package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.features.playlist_overview.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.features.player.data.model.PlayerStateDto
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserFullDto

interface SpotifyWebApi {
    suspend fun getCurrentUsersPlaylists(): Result<PlaylistResponseDto>

    suspend fun getTracksForPlaylist(playlistId: String, limit: Int, offset: Int = 0): Result<TracksResponseDto>

    suspend fun getCurrentUser(): Result<UserFullDto>

    suspend fun getUserForId(userId: String): Result<UserFullDto>

    suspend fun playTrack(playlistId: String, trackId: String): Result<Unit?>

    suspend fun pauseTrack(): Result<Unit?>

    suspend fun seekPosition(newPosMs: Long): Result<Unit?>

    suspend fun getPlayerState(): Result<PlayerStateDto>
}