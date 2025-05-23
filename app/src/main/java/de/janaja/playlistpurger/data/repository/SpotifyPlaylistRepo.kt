package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.mapper.toPlaylist
import de.janaja.playlistpurger.data.remote.spotify.SpotifyWebApiService
import de.janaja.playlistpurger.domain.exception.DataException
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import kotlinx.coroutines.flow.firstOrNull

class SpotifyPlaylistRepo(
    authService: AuthService,
    private val webApiService: SpotifyWebApiService
) : PlaylistRepo {

    private val tokenFlow = authService.accessToken

    override suspend fun getPlaylists(): Result<List<Playlist>> {
        val token =
            tokenFlow.firstOrNull() ?: return Result.failure(DataException.Auth.MissingAccessToken)

        val result = webApiService.getCurrentUsersPlaylists("Bearer $token")

        return result.fold(
            onSuccess = { playlistResponse ->
                Result.success(playlistResponse.items.map { it.toPlaylist() })
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}