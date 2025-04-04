package de.janaja.playlistpurger.data.repository

import android.util.Log
import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.data.remote.SpotifyApi
import de.janaja.playlistpurger.data.remote.VoteApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class TrackListRepoImpl(
    dataStoreRepo: DataStoreRepo,
    val voteApi: VoteApi
) : TrackListRepo {
    val TAG = "TrackListRepo"

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.accessTokenFlow
        .onEach {

        }
    private val currentUserId = "janajansen-de" // TODO get real current user id from somewhere

    override val allTracks = MutableStateFlow<List<Track>>(listOf())

    override val unvotedTracks: Flow<List<Track>> = allTracks.map { list ->
        list.filter { it.vote == null }
    }.onEach {
        Log.d(TAG, "unvotedTracks: updatet $it")
    }

    override suspend fun loadTracksWithVotes(playlistId: String) {
        val token = tokenFlow.first() ?: throw Exception("No Token")
        val tracksResponse = api.getTracksForPlaylist("Bearer $token", playlistId)

        // TODO parallel
        val tracksFromSpotify = tracksResponse.items.map { it.track }
        val votesForPlaylist = voteApi.getVotesForPlaylist(playlistId, currentUserId)

        val mergedTracks =
            tracksFromSpotify.map { track -> track.copy(vote = votesForPlaylist.firstOrNull { it.trackId == track.id }?.voteOption) }

        allTracks.value = mergedTracks

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