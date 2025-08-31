package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.features.playlist_overview.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserFullDto

interface SpotifyWebApi {
    suspend fun getCurrentUsersPlaylists(): Result<PlaylistResponseDto>

    suspend fun getTracksForPlaylist(playlistId: String, ): Result<TracksResponseDto>

    suspend fun getCurrentUser(): Result<UserFullDto>

    suspend fun getUserForId(userId: String): Result<UserFullDto>
}