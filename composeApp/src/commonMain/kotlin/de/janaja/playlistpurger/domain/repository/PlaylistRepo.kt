package de.janaja.playlistpurger.domain.repository

import de.janaja.playlistpurger.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepo {

    fun getPlaylists(): Flow<Result<List<Playlist>>>

}