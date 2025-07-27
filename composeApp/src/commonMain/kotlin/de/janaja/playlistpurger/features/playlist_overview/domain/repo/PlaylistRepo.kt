package de.janaja.playlistpurger.features.playlist_overview.domain.repo

import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepo {

    suspend fun getPlaylists(): Result<List<Playlist>>

}