package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.shared.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserDto

interface SpotifyWebApi {
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

    suspend fun getUserForId(
        token: String,
        userId: String
    ): Result<UserDto>
}