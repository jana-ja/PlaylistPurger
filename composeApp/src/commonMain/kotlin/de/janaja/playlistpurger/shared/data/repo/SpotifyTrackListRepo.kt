package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.util.ConcurrentLruCache
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.shared.data.model.toTrack
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.data.remote.VoteApi
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.TrackAdder
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SpotifyTrackListRepo(
    authService: AuthService,
    private val voteApi: VoteApi,
    private val webApi: SpotifyWebApi,
    private val userRepo: UserRepo

) : TrackListRepo {

    private companion object {

        const val TAG = "TrackListRepo"
        const val MAX_CACHED_PLAYLISTS = 5
    }

    private val tokenFlow = authService.accessToken
    private val userFlow = authService.userLoginState.map { state ->
        if (state is UserLoginState.LoggedIn)
            return@map state.user
        else
            return@map null
    }

    private val trackListCache: MutableMap<String, List<Track>> = ConcurrentLruCache(MAX_CACHED_PLAYLISTS)

    // repo handles state
    private val currentTracksWithOwnVotes = MutableStateFlow<List<Track>>(listOf())

    override fun observeCurrentPlaylistTracks() = currentTracksWithOwnVotes.asStateFlow()

    override suspend fun loadCurrentPlaylistTracks(playlistId: String): Result<Unit> {

        return withContext(Dispatchers.IO) {
            trackListCache[playlistId]?.let { cachedTrackList ->
                Log.d(TAG, "loadCurrentPlaylistTracks: found trackList in cache")
                currentTracksWithOwnVotes.value = cachedTrackList
                return@withContext Result.success(Unit)
            }

            Log.d(
                TAG,
                "loadCurrentPlaylistTracks: did not find trackList in cache. try loading from api"
            )
            val token =
                tokenFlow.first() ?: return@withContext Result.failure(DataException.Auth.MissingAccessToken)
            val currentUserId =
                userFlow.first()?.id ?: return@withContext Result.failure(DataException.Auth.MissingCurrentUser)

            val tracksResult = webApi.getTracksForPlaylist("Bearer $token", playlistId)

            return@withContext tracksResult.fold(
                onSuccess = { tracksResponse ->
                    val tracksFromSpotify = tracksResponse.items//.map { it.track }
                    val votesResult = voteApi.getUsersVotesForPlaylist(playlistId, currentUserId)

                    votesResult.fold(
                        onSuccess = { votesForPlaylist ->
                            // load users
                            val uniqueUserIds = tracksFromSpotify
                                .map { it.addedBy.id }
                                .distinct()
                            val userMap = uniqueUserIds.associateWith {
                                userRepo.getUserForId(it).fold(
                                    onSuccess = { user -> user },
                                    onFailure = { null }
                                )
                            }

                            val mergedTracks =
                                tracksFromSpotify.map { trackWrapper ->
                                    val userId = trackWrapper.addedBy.id
                                    val user = userMap[trackWrapper.addedBy.id]
                                    val trackAdderInfo: TrackAdder = if (user != null) TrackAdder.FullUser(user) else TrackAdder.IdOnly(userId)

                                    trackWrapper.toTrack(
                                        votesForPlaylist.firstOrNull { it.trackId == trackWrapper.track.id }?.voteOption,
                                        trackAdderInfo
                                        )

                                }

                            currentTracksWithOwnVotes.value = mergedTracks
                            trackListCache[playlistId] = mergedTracks

                            Result.success(Unit)
                        },
                        onFailure = { e ->
                            Result.failure(e)
                        }
                    )
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }
    }

    override suspend fun loadTracksWithAllVotes(playlistId: String): Result<List<Pair<Track, List<Vote>>>> {

        loadCurrentPlaylistTracks(playlistId)

        // get all votes
        val allVotesResult = voteApi.getAllVotesForPlaylist(playlistId)
        return allVotesResult.fold(
            onSuccess = { voteDtoList ->

                val uniqueUserIds = voteDtoList.map { it.userId }.distinct()

                val userMap = uniqueUserIds.associateWith {
                    userRepo.getUserForId(it).fold(
                        onSuccess = { user -> user },
                        onFailure = { null }
                    )
                }

                val voteList = voteDtoList.map {
                    val user = userMap[it.userId]
                    // TODO how to handle null in app?
                    Vote(
                        playlistId = it.playlistId,
                        trackId = it.trackId,
                        user = user,
                        voteOption = it.voteOption
                    )
                }
                val allVotesByTrackId = voteList.groupBy { it.trackId }
                Result.success(
                    currentTracksWithOwnVotes.value.map { Pair(it, allVotesByTrackId[it.id] ?: emptyList()) }
                )
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }


    override suspend fun upsertVote(
        playlistId: String,
        trackId: String,
        newVote: VoteOption
    ): Result<Unit> {
        val currentUserId =
            userFlow.first()?.id ?: return Result.failure(DataException.Auth.MissingCurrentUser)

        // store old value
        val trackToUpdate = currentTracksWithOwnVotes.value.find { it.id == trackId }
        if (trackToUpdate == null) {
            return Result.failure(DataException.Remote.Unknown) // TODO right exception
        }
        val oldValue = trackToUpdate.vote

        // update locally
        currentTracksWithOwnVotes.value = currentTracksWithOwnVotes.value.map {
            if (it.id == trackId)
                it.copy(vote = newVote)
            else
                it
        }
        // update globally
        val updateResult = voteApi.upsertVote(playlistId, trackId, currentUserId, newVote)
        return updateResult.fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = {
                // rollback
                currentTracksWithOwnVotes.value = currentTracksWithOwnVotes.value.map {
                    if (it.id == trackId) {
                        it.copy(vote = oldValue)
                    } else {
                        it
                    }
                }
                Log.e(
                    TAG,
                    "Failed to update vote on Api, rolled back.",
                    DataException.Remote.Unknown
                ) //TODO real exception
                Result.failure(DataException.Remote.Unknown)

            }
        )
    }


}