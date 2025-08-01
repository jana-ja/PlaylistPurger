package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.features.playlist_overview.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserFullDto

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
    ): Result<UserFullDto>

    suspend fun getUserForId(
        token: String,
        userId: String
    ): Result<UserFullDto>
}