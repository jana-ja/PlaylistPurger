package de.janaja.playlistpurger.features.playlist_overview.data.repo

import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.shared.data.model.toPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class SpotifyPlaylistRepo(
    authService: AuthService,
    private val webApi: SpotifyWebApi
) : PlaylistRepo {

    private val tokenFlow = authService.accessToken

    override fun getPlaylists(): Flow<Result<List<Playlist>>> = flow {
        val token =
            tokenFlow.firstOrNull() ?: emit(
                Result.failure(DataException.Auth.MissingAccessToken)
            )
        val result = webApi.getCurrentUsersPlaylists("Bearer $token")

        emit(
            result.fold(
                onSuccess = { playlistResponse ->
                    Result.success(playlistResponse.items.map { it.toPlaylist() })
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        )
    }
}