package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.util.ConcurrentCache
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.shared.data.model.toUser
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.domain.model.UserWithName
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.flow.first

class SpotifyUserRepo(
    authService: AuthService,
    private val webApi: SpotifyWebApi
) : UserRepo {

    private val tokenFlow = authService.accessToken

    // cache
    // concurrent hash map
    // TODO LRU (least recently used) mit number of items
    // no invalidation, user image could become stale but thats okay
    // no time to live (ttl)
    // TODO is this okay or does it need to be UserDetails.Full? then imageUrl would have to be nullable
    private val userCache: MutableMap<String, UserWithName> = ConcurrentCache()

    override suspend fun getUserForId(userId: String): Result<UserWithName> {
        userCache[userId]?.let { cachedUser ->
            return Result.success(cachedUser)
        }

        val token =
            tokenFlow.first() ?: return Result.failure(DataException.Auth.MissingAccessToken)
        webApi.getUserForId("Bearer $token", userId).fold(
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