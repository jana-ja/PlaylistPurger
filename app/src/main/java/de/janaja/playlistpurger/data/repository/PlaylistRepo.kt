package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Playlist

interface PlaylistRepo {

    suspend fun getPlaylists(): List<Playlist>

}