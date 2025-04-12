package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistDto
import de.janaja.playlistpurger.data.remote.spotify.SpotifyApi
import kotlinx.coroutines.flow.first

class SpotifyPlaylistRepo(
    dataStoreRepo: DataStoreRepo
): PlaylistRepo {
    val TAG = "PlaylistRepoImpl"

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.accessTokenFlow

    override suspend fun getPlaylists(): List<PlaylistDto> {
        val token = tokenFlow.first() ?: throw Exception("No Token")

        val playListResponse = api.getCatImagesWithHeader("Bearer $token")
        return playListResponse.items
    }

}