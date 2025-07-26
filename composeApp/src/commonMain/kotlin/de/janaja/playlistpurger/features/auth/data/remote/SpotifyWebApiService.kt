package de.janaja.playlistpurger.features.auth.data.remote

import de.janaja.playlistpurger.shared.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserDto


interface SpotifyWebApiService {
    suspend fun getCurrentUsersPlaylists(
        token: String
    ): Result<PlaylistResponseDto>

    suspend fun getTracksForPlaylist(
        token: String,
        playlistId: String,
    ): Result<TracksResponseDto>

    suspend fun getCurrentUser(
        token: String,
    ): Result<UserDto>
}