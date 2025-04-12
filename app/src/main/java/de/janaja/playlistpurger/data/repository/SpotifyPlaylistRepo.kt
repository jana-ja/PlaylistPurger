package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.mapper.toPlaylist
import de.janaja.playlistpurger.data.remote.spotify.SpotifyApi
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.repository.DataStoreRepo
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import kotlinx.coroutines.flow.first

class SpotifyPlaylistRepo(
    dataStoreRepo: DataStoreRepo
): PlaylistRepo {

    private val api = SpotifyApi.retrofitService

    private val tokenFlow = dataStoreRepo.accessTokenFlow

    override suspend fun getPlaylists(): List<Playlist> {
        val token = tokenFlow.first() ?: throw Exception("No Token")

        val playListResponse = api.getCatImagesWithHeader("Bearer $token")
        return playListResponse.items.map { it.toPlaylist() }
    }

}