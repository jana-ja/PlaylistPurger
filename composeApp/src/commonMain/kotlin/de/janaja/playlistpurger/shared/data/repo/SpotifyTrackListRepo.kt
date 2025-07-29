package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.model.LoginState
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.shared.data.model.toTrack
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.data.remote.VoteApi
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SpotifyTrackListRepo(
    authService: AuthService,
    private val voteApi: VoteApi,
    private val webApi: SpotifyWebApi,
    private val userRepo: UserRepo

) : TrackListRepo {

    private val TAG = "TrackListRepo"

    private val tokenFlow = authService.accessToken
    private val userFlow = authService.loginState.map { state ->
        if (state is LoginState.LoggedIn)
            return@map state.user
        else
            return@map null
    }

    // repo handles state
    // TODO map with id or something to cache multiple playlists
    private val allTracksWithOwnVotes = MutableStateFlow<List<Track>>(listOf())

    override fun observeCurrentPlaylistTracks() = allTracksWithOwnVotes.asStateFlow()

    override suspend fun refreshTracksWithOwnVotes(playlistId: String): Result<Unit> {
        // TODO cache lists for ids in memory?
        val token =
            tokenFlow.first() ?: return Result.failure(DataException.Auth.MissingAccessToken)
        val currentUserId =
            userFlow.first()?.id ?: return Result.failure(DataException.Auth.MissingCurrentUser)

        val tracksResult = webApi.getTracksForPlaylist("Bearer $token", playlistId)

        return tracksResult.fold(
            onSuccess = { tracksResponse ->
                // TODO parallel
                val tracksFromSpotify = tracksResponse.items.map { it.track }
                val votesResult = voteApi.getUsersVotesForPlaylist(playlistId, currentUserId)

                votesResult.fold(
                    onSuccess = { votesForPlaylist ->
                        val mergedTracks =
                            tracksFromSpotify.map { track -> track.toTrack(votesForPlaylist.firstOrNull { it.trackId == track.id }?.voteOption) }

                        allTracksWithOwnVotes.value = mergedTracks

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

    override suspend fun loadTracksWithAllVotes(playlistId: String): Result<List<Pair<Track, List<Vote>>>> {

//        if (allTracks.value.isEmpty()) {
//            // TODO error handling??
        refreshTracksWithOwnVotes(playlistId)
//        }

        // get all votes
        val allVotesResult = voteApi.getAllVotesForPlaylist(playlistId)
        return allVotesResult.fold(
            onSuccess = { voteDtoList ->
                // TODO load each user just once, maybe in UserRepo with in-memory cache map of user ids and user
                //  then coordinate from use case?
                val voteList = voteDtoList.map {
                    val user = userRepo.getUserForId(it.userId)
                        .fold(
                            onSuccess = { user -> user },
                            onFailure = { null }
                        )
                    Vote(
                        playlistId = it.playlistId,
                        trackId = it.trackId,
                        user = user,
                        voteOption = it.voteOption
                    )
                }
                val allVotesByTrackId = voteList.groupBy { it.trackId }
                Result.success(
                    allTracksWithOwnVotes.value.map { Pair(it, allVotesByTrackId[it.id] ?: emptyList()) }
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
        val trackToUpdate = allTracksWithOwnVotes.value.find { it.id == trackId }
        if (trackToUpdate == null) {
            return Result.failure(DataException.Remote.Unknown) // TODO right exception
        }
        val oldValue = trackToUpdate.vote

        // update locally
        allTracksWithOwnVotes.value = allTracksWithOwnVotes.value.map {
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
                allTracksWithOwnVotes.value = allTracksWithOwnVotes.value.map {
                    if (it.id == trackId) {
                        it.copy(vote = oldValue)
                    } else {
                        it
                    }
                }
                Log.e(TAG, "Failed to update vote on Api, rolled back.", DataException.Remote.Unknown) //TODO real exception
                Result.failure(DataException.Remote.Unknown)

            }
        )
    }


}