package de.janaja.playlistpurger.features.playlist_overview.data.repo

import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.features.playlist_overview.data.model.toPlaylist
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.flow.firstOrNull

class SpotifyPlaylistRepo(
    authService: AuthService,
    private val webApi: SpotifyWebApi,
    private val userRepo: UserRepo
) : PlaylistRepo {

    private val tokenFlow = authService.accessToken

    override suspend fun getPlaylists(): Result<List<Playlist>> {
        val token =
            tokenFlow.firstOrNull() ?: return Result.failure(DataException.Auth.MissingAccessToken)

        val result = webApi.getCurrentUsersPlaylists("Bearer $token")

        return result.fold(
            onSuccess = { playlistResponse ->

                Result.success(playlistResponse.items.map {
                    val user = userRepo.getUserForId(it.owner.id)
                        .fold(
                            onSuccess = { user -> user },
                            onFailure = { null }
                        )
                    it.toPlaylist(user)
                })
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}