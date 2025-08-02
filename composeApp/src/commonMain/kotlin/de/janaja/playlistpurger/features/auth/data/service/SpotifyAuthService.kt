package de.janaja.playlistpurger.features.auth.data.service

import de.janaja.playlistpurger.features.auth.data.remote.SpotifyAccountApi
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.hiddenClientSecret
import de.janaja.playlistpurger.shared.data.model.toUser
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SpotifyAuthService(
    private val tokenRepo: TokenRepo,
    private val webApi: SpotifyWebApi,
    private val accountApi: SpotifyAccountApi
) : AuthService {
    private val TAG = "AuthService"

    private val clientSecret = hiddenClientSecret // TODO switch to kcmp
    private val clientId = "1f7401f5d27847b99a6dfe6908c5ccac"
    private val redirectUri = "asdf://callback"

    override val accessToken = tokenRepo.accessTokenFlow

    private val refreshTokenFlow = tokenRepo.refreshTokenFlow

    override val userLoginState = accessToken
        .map { checkSavedToken(it) }


    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun loginWithCode(code: String): Result<Unit> {
        // TODO loginState -> loading
        Log.d(TAG, "loginWithCode: try receive token for code: $code")

        val result = this.accountApi.getToken(
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
                Log.e(TAG, "loginWithCode: ", e)
                Result.failure(e)
            }
        )


    }

    private suspend fun checkSavedToken(token: String?): UserLoginState {
        Log.d(TAG, "received token: $token")

        if (token == null) {
            Log.d(TAG, "no saved token -> logged out")
            return UserLoginState.LoggedOut
        } else {
            Log.d(TAG, "found saved token -> check if valid by loading current user")
            val result = this.webApi.getCurrentUser(token)

            result.onSuccess { user ->
                Log.d(TAG, "token is valid -> logged in")
                return UserLoginState.LoggedIn(user.toUser())
            }.onFailure { e ->
                // TODO wenn man kein internet hat gibt: java.net.UnknownHostException
                //  grade endlos splash screen
                Log.e(TAG, "loading current user failed: ", e)
                when (e) {
                    DataException.Remote.InvalidAccessToken -> {
                        Log.d(TAG, "token is not valid - try to refresh")

                        if (refreshTokenOrLogout().isFailure) {
                            // TODO show something to user, maybe go default exception handling function path
                            return UserLoginState.LoggedOut

                        }
                        // TODO if successful try to load user again?
                    }
                }
                // TODO improve error handling and think about loadingState flows in exception case (endless refresh/reload or loadingState?)
            }
            return UserLoginState.Loading
        }
    }

    override fun getAuthenticationUrl(): String {
        val urlString = "https://accounts.spotify.com/de/authorize?" +
                "scope=playlist-read-private" +
                "&response_type=code" +
                "&redirect_uri=$redirectUri" + // asdf%3A%2F%2Fcallback
                "&client_id=$clientId" +
                "&show_dialog=true"
        return urlString
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun refreshTokenOrLogout(): Result<Unit> {
        // unsuccessful -> call logout - this will delete token and trigger checkToken to set the loginState
        // successful -> update access token - this will trigger checkToken to set the loginState

        val value = refreshTokenFlow.first()

        Log.d(TAG, "refresh token collected: $value")

        if (value == null) {
            Log.d(TAG, "no saved refresh token -> logout to delete all tokens")
            logout()
            return Result.failure(DataException.Auth.MissingOrInvalidRefreshToken)
        } else {
            // TODO improve error handling
            Log.d(TAG, "found saved refresh token -> get fresh access token with it")
            val result = this.accountApi.refreshToken(
                client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                refreshToken = value,
            )

            result.fold(
                onSuccess = { tokenRequestResponse ->
                    Log.d(TAG, "got fresh access token -> save it")
                    tokenRepo.updateAccessToken(tokenRequestResponse.accessToken)
                    if (tokenRequestResponse.refreshToken != "") {
                        tokenRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
                    }
                    return Result.success(Unit)
                },
                onFailure = { e ->
                    Log.e(TAG, "refreshToken: getting fresh access token failed. Logging out", e)
                    logout()
                    return Result.failure(e)
                }
            )
        }
    }


    override suspend fun logout() {
        tokenRepo.deleteAllToken()
    }


}