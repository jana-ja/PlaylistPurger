package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.remote.SpotifyApi
import kotlinx.coroutines.flow.first

class TrackListRepoImpl(
    dataStoreRepo: DataStoreRepo
): TrackListRepo {
    val TAG = "TrackListRepo"

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.tokenFlow

    override suspend fun getTracks(playlistId: String): List<Track> {
        val token = tokenFlow.first() ?: throw Exception("No Token")

        val tracksResponse = api.getTracksForPlaylist("Bearer $token", playlistId)
        return tracksResponse.items.map { it.track }
    }

    override fun updateVote(track: Track, playlistId: String) {
       // TODO
    }


}