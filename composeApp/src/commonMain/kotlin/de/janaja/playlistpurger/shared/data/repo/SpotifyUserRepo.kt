package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.shared.data.model.toUser
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.domain.model.User
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.flow.first

class SpotifyUserRepo(
    authService: AuthService,
    private val webApi: SpotifyWebApi
) : UserRepo {

    private val tokenFlow = authService.accessToken

    override suspend fun getUserForId(userId: String): Result<User> {
        val token =
            tokenFlow.first() ?: return Result.failure(DataException.Auth.MissingAccessToken)
        webApi.getUserForId("Bearer $token", userId).fold(
            onSuccess = {
                return Result.success(it.toUser())
            },
            onFailure = {
                return Result.failure(it)
            }
        )
    }
}