package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.mapper.toTrack
import de.janaja.playlistpurger.data.remote.spotify.SpotifyWebApiService
import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.data.remote.vote.VoteApi
import de.janaja.playlistpurger.domain.exception.DataException
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.TrackListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SpotifyTrackListRepo(
    authService: AuthService,
    private val voteApi: VoteApi,
    private val webApiService: SpotifyWebApiService

) : TrackListRepo {

    private val tokenFlow = authService.accessToken
    private val userFlow = authService.loginState.map { state ->
        if (state is LoginState.LoggedIn)
            return@map state.user
        else
            return@map null
    }

    private val allTracks = MutableStateFlow<List<Track>>(listOf())

    override suspend fun loadTracksWithOwnVotes(playlistId: String): Result<Flow<List<Track>>> {
        // TODO cache lists for ids in memory?
        val token =
            tokenFlow.first() ?: return Result.failure(DataException.Auth.MissingAccessToken)
        val currentUserId =
            userFlow.first()?.id ?: return Result.failure(DataException.Auth.MissingCurrentUser)

        val tracksResult = webApiService.getTracksForPlaylist("Bearer $token", playlistId)

        // TODO alternativ: flows nehmen die nur einmal emiten, dann auch möglich retry nach x sek und combine benutzen
        //  flow<result>
        return tracksResult.fold(
            onSuccess = { tracksResponse ->
                // TODO parallel
                val tracksFromSpotify = tracksResponse.items.map { it.track }
                val votesResult = voteApi.getVotesForPlaylist(playlistId, currentUserId)

                votesResult.fold(
                    onSuccess = { votesForPlaylist ->
                        val mergedTracks =
                            tracksFromSpotify.map { track -> track.toTrack(votesForPlaylist.firstOrNull { it.trackId == track.id }?.voteOption) }

                        allTracks.value = mergedTracks

                        Result.success(allTracks)
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
        loadTracksWithOwnVotes(playlistId)
//        }

        // get all votes
        val allVotesResult = voteApi.getAllVotesForPlaylist(playlistId)
        return allVotesResult.fold(
            onSuccess = { allVotes ->
                val allVotesByTrackId = allVotes.groupBy { it.trackId }
                Result.success(
                    allTracks.value.map { Pair(it, allVotesByTrackId[it.id] ?: emptyList()) }
                )
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun updateVote(
        playlistId: String,
        trackId: String,
        newVote: VoteOption
    ): Result<Unit> {
        val currentUserId =
            userFlow.first()?.id ?: return Result.failure(DataException.Auth.MissingCurrentUser)

        voteApi.upsertVote(playlistId, trackId, currentUserId, newVote)
        // TODO auf antwort warten, dabei feedback für user anzeigen, dann lokal ändern?
        //  oder lokal ändern und alten wert cachen, dann updaten, wenn fehler user anzeigen und zurück nehmen.
        allTracks.value = allTracks.value.map {
            if (it.id == trackId)
                it.copy(vote = newVote)
            else
                it
        }
        return Result.success(Unit)
    }


}