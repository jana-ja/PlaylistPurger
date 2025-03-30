package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.remote.SpotifyApi
import kotlinx.coroutines.flow.first

class PlayListRepoImpl(
    dataStoreRepo: DataStoreRepo
): PlayListRepo {
    val TAG = "PlayListRepoImpl"

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.tokenFlow

    override suspend fun getPlaylists(): List<Playlist> {
        val token = tokenFlow.first() ?: throw Exception("No Token")

        val playListResponse = api.getCatImagesWithHeader("Bearer $token")
        return playListResponse.items
    }

}