package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.util.ConcurrentLruCache
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import de.janaja.playlistpurger.shared.data.model.toUser
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.domain.model.UserDetails
import de.janaja.playlistpurger.shared.domain.repository.UserRepo

class SpotifyUserRepo(
    authService: AuthService,
    private val webApi: SpotifyWebApi
) : UserRepo {

    private val tokenFlow = authService.accessToken

    // no invalidation, user image could become stale but thats okay
    // no time to live (ttl)
    private val userCache: MutableMap<String, UserDetails.Full> = ConcurrentLruCache(150)

    override suspend fun getUserForId(userId: String): Result<UserDetails.Full> {
        userCache[userId]?.let { cachedUser ->
            return Result.success(cachedUser)
        }

        val tokenState = tokenFlow.value
        when (tokenState) {
            TokenState.Loading -> {
                return Result.failure(DataException.Auth.TokenNotReady)
            }
            TokenState.NoToken -> {
                return Result.failure(DataException.Auth.MissingAccessToken)
            }
            is TokenState.Loaded -> {
                val token = tokenState.token
                webApi.getUserForId(token, userId).fold(
                    onSuccess = {
                        val user = it.toUser()
                        userCache[userId] = user
                        return Result.success(user)
                    },
                    onFailure = {
                        return Result.failure(it)
                    }
                )
            }
        }
    }
}