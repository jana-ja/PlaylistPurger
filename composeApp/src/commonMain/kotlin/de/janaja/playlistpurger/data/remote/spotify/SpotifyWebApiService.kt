package de.janaja.playlistpurger.data.remote.spotify

import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistResponseDto
import de.janaja.playlistpurger.data.remote.spotify.model.TracksResponseDto
import de.janaja.playlistpurger.data.remote.spotify.model.UserDto

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