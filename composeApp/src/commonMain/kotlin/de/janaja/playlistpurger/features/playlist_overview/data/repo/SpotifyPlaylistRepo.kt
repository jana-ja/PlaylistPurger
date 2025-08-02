package de.janaja.playlistpurger.features.playlist_overview.data.repo

import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.features.playlist_overview.data.model.toPlaylist
import de.janaja.playlistpurger.shared.data.model.toUserDetails
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

        val result = webApi.getCurrentUsersPlaylists(token)

        return result.fold(
            onSuccess = { playlistResponse ->

                val uniqueUserIds = playlistResponse.items.map { it.owner.id }.distinct()

                val userMap = uniqueUserIds.associateWith {
                    userRepo.getUserForId(it).fold(
                        onSuccess = { user -> user },
                        onFailure = { null }
                    )
                }

                Result.success(playlistResponse.items.map {
                    val userDto = it.owner
                    val userDetails = userMap[it.owner.id] ?: userDto.toUserDetails()
                    it.toPlaylist(userDetails)
                })
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}