package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.data.remote.SpotifyApi
import de.janaja.playlistpurger.data.remote.VoteApi
import kotlinx.coroutines.flow.first

class TrackListRepoImpl(
    dataStoreRepo: DataStoreRepo,
    val voteApi: VoteApi
): TrackListRepo {
    val TAG = "TrackListRepo"

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.tokenFlow
    private val currentUserId = "janajansen-de" // TODO get real current user id from somewhere


    override suspend fun getTracksWithVotes(playlistId: String): List<Track> {
        val token = tokenFlow.first() ?: throw Exception("No Token")
        val tracksResponse = api.getTracksForPlaylist("Bearer $token", playlistId)

        // TODO parallel
        val tracksFromSpotify = tracksResponse.items.map { it.track }
        val votesForPlaylist = voteApi.getVotesForPlaylist(playlistId, currentUserId)

        val mergedTracks = tracksFromSpotify.map { track -> track.copy(vote = votesForPlaylist.firstOrNull{it.trackId == track.id}?.voteOption) }

        return mergedTracks
    }

    override fun updateVote(playlistId: String, trackId: String, newVote: VoteOption) {
       voteApi.upsertVote(playlistId, trackId, currentUserId, newVote)
        // TODO auf antwort warten, dabei feedback für user anzeigen, dann lokal ändern?
        //  oder lokal ändern und alten wert cachen, dann updaten, wenn fehler user anzeigen und zurück nehmen.

    }


}