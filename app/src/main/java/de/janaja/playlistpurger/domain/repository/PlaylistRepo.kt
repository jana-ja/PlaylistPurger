package de.janaja.playlistpurger.domain.repository

import de.janaja.playlistpurger.domain.model.Playlist

interface PlaylistRepo {

    suspend fun getPlaylists(): Result<List<Playlist>>

}