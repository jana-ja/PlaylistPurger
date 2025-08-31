package de.janaja.playlistpurger.shared.data.repo

import de.janaja.playlistpurger.core.util.ConcurrentLruCache
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import de.janaja.playlistpurger.shared.data.model.toUser
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.domain.model.UserDetails
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class SpotifyUserRepo(
    tokenRepo: TokenRepo,
    private val webApi: SpotifyWebApi,
    externalScope: CoroutineScope
) : UserRepo {

    companion object {
        private const val TAG = "SpotifyUserRepo"
    }

    // this is a hot flow, but this does not mean that data from upstream is processed even when there is no subscriber
    // solution for this case -> SharingStarted.Eagerly because token and user have to be available all the time anyway
    override val currentUser = tokenRepo.accessTokenFlow
        .map { getUserForToken(it) }
        .onEach {
            Log.d(TAG, "currentUser: $it")
        }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.Eagerly, // TODO rethink this
            // WhileSubscribed is not sufficient right now because there are no active collectors, only the value is read in TrackListRepo
            initialValue = null
        )

    // no invalidation, user image could become stale but thats okay
    // no time to live (ttl)
    private val userCache: MutableMap<String, UserDetails.Full> = ConcurrentLruCache(150)

    override suspend fun getUserForId(userId: String): Result<UserDetails.Full> {
        userCache[userId]?.let { cachedUser ->
            return Result.success(cachedUser)
        }

        webApi.getUserForId(userId).fold(
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

    private suspend fun getUserForToken(tokenState: TokenState): UserDetails.Full? {
        Log.i(TAG, "getUserForToken: $tokenState")
        return when (tokenState) {
            is TokenState.HasToken -> {
                val result = webApi.getCurrentUser()
                result.fold(
                    onSuccess = {
                        val user = it.toUser()
                        Log.i(TAG, "getUserForToken: success $user")
                        user
                    },
                    onFailure = {
                        Log.e(TAG, "getUserForToken: failed to get user for token")
                        // TODO hier noch mehr machen?
                        null
                    }
                )
            }

            else -> {
                Log.e(TAG, "getUserForToken: no token available")
                null
            }
        }
    }
}