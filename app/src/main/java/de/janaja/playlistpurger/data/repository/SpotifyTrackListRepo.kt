package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.remote.spotify.model.TrackDto
import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.data.remote.spotify.SpotifyApi
import de.janaja.playlistpurger.data.remote.VoteApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach

class SpotifyTrackListRepo(
    dataStoreRepo: DataStoreRepo,
    val voteApi: VoteApi
) : TrackListRepo {
    val TAG = "TrackListRepo"

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.accessTokenFlow
        .onEach {

        }
    private val currentUserId = "janajansen-de" // TODO get real current user id from somewhere

    override val allTracks = MutableStateFlow<List<TrackDto>>(listOf())

//    override val unvotedTracks: Flow<List<Track>> = allTracks.map { list ->
//        list.filter { it.vote == null }
//    }.onEach {
//        Log.d(TAG, "unvotedTracks: updatet $it")
//    }

    override suspend fun loadTracksWithOwnVotes(playlistId: String) {
        val token = tokenFlow.first() ?: throw Exception("No Token")
        val tracksResponse = api.getTracksForPlaylist("Bearer $token", playlistId)

        // TODO parallel
        val tracksFromSpotify = tracksResponse.items.map { it.track }
        val votesForPlaylist = voteApi.getVotesForPlaylist(playlistId, currentUserId)

        val mergedTracks =
            tracksFromSpotify.map { track -> track.copy(vote = votesForPlaylist.firstOrNull { it.trackId == track.id }?.voteOption) }

        allTracks.value = mergedTracks

    }

    override suspend fun loadTracksWithAllVotes(playlistId: String): List<Pair<TrackDto, List<Vote>>> {
        if (allTracks.value.isEmpty()) {
            loadTracksWithOwnVotes(playlistId)
        }

        // get all votes
        val allVotesByTrackId = voteApi.getAllVotesForPlaylist(playlistId).groupBy { it.trackId }

        return allTracks.value.map { Pair(it, allVotesByTrackId[it.id] ?: emptyList()) }

    }

    override fun updateVote(playlistId: String, trackId: String, newVote: VoteOption) {
        voteApi.upsertVote(playlistId, trackId, currentUserId, newVote)
        // TODO auf antwort warten, dabei feedback für user anzeigen, dann lokal ändern?
        //  oder lokal ändern und alten wert cachen, dann updaten, wenn fehler user anzeigen und zurück nehmen.
        allTracks.value = allTracks.value.map {
            if (it.id == trackId)
                it.copy(vote = newVote)
            else
                it
        }
    }




}