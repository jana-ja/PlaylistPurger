package de.janaja.playlistpurger.data.repository

import android.util.Log
import de.janaja.playlistpurger.BuildConfig
import de.janaja.playlistpurger.data.mapper.toUser
import de.janaja.playlistpurger.data.remote.spotify.SpotifyAccountApiService
import de.janaja.playlistpurger.data.remote.spotify.SpotifyWebApiService
import de.janaja.playlistpurger.domain.exception.DataException
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.TokenRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SpotifyAuthService(
    private val tokenRepo: TokenRepo,
    private val webApiService: SpotifyWebApiService,
    private val accountApiService: SpotifyAccountApiService
) : AuthService {
    private val TAG = "AuthRepo"

    private val clientSecret = BuildConfig.CLIENT_SECRET
    private val clientId = BuildConfig.CLIENT_ID
    private val redirectUri = "asdf://callback"

    override val accessToken = tokenRepo.accessTokenFlow

    private val refreshTokenFlow = tokenRepo.refreshTokenFlow

    override val loginState = accessToken
        .map { checkToken(it) }


    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun loginWithCode(code: String): Result<Unit> {
        // TODO loginState -> loading
        Log.d(TAG, "loginWithCode: try receive token for code: $code")

        val result = this.accountApiService.getToken(
            client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
            code = code,
            redirectUri = redirectUri,
        )
        return result.fold(
            onSuccess = { tokenRequestResponse ->
                Log.d(TAG, "loginWithCode: got token for toke")
                tokenRepo.updateAccessToken(tokenRequestResponse.accessToken)
                tokenRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
                Result.success(Unit)
            },
            onFailure = { e ->
                Log.e(TAG, "loginWithCode: ", e.cause)
                Result.failure(e)
            }
        )


    }

    private suspend fun checkToken(token: String?): LoginState {
        Log.d(TAG, "received token: $token")

        if (token == null) {
            Log.d(TAG, "no saved token -> logged out")
            return LoginState.LoggedOut
        } else {
            Log.d(TAG, "found saved token -> check if valid by loading current user")
            val result = this.webApiService.getCurrentUser("Bearer $token")

            result.onSuccess { user ->
                Log.d(TAG, "token is valid -> logged in")
                return LoginState.LoggedIn(user.toUser())
            }.onFailure { e ->
                Log.d(TAG, "loading current user failed: ", e.cause)
                when (e) {
                    DataException.Remote.InvalidAccessToken -> {
                        Log.d(TAG, "token is not valid - try to refresh")

                        if (!refreshToken()) {
                            // TODO show something to user, maybe go default exception handling function path
                            logout()
                        }
                    }
                }
                // TODO improve error handling and think about loadingState flows in exception case (endless refresh/reload or loadingState?)
            }
            return LoginState.Loading
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun refreshToken(): Boolean {
        // unsuccessful -> call logout - this will delete token and trigger checkToken to set the loginState
        // successful -> update access token - this will trigger checkToken to set the loginState

        val value = refreshTokenFlow.first()

        Log.d(TAG, "refresh token collected: $value")

        if (value == null) {
            Log.d(TAG, "no saved refresh token -> logout to delete all tokens")
            logout()
            return false
        } else {
            // TODO improve error handling
            Log.d(TAG, "found saved refresh token -> get fresh access token with it")
            val result = this.accountApiService.refreshToken(
                client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                refreshToken = value,
            )

            result.onSuccess { tokenRequestResponse ->
                Log.d(TAG, "got fresh access token -> save it")
                tokenRepo.updateAccessToken(tokenRequestResponse.accessToken)
                if (tokenRequestResponse.refreshToken != "") {
                    tokenRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
                }
                return true
            }.onFailure { e ->
                Log.e(TAG, "refreshToken: Error: ${e.localizedMessage}")
                return false
            }
            // TODO
            return false
        }
    }


    override suspend fun logout() {
        tokenRepo.deleteAllToken()
    }


}