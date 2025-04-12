package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistDto

interface PlaylistRepo {

    suspend fun getPlaylists(): List<PlaylistDto>

}