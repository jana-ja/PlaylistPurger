package de.janaja.playlistpurger.data.repository

import android.util.Log
import de.janaja.playlistpurger.BuildConfig
import de.janaja.playlistpurger.data.remote.spotify.SpotifyAccountApi
import de.janaja.playlistpurger.data.remote.spotify.SpotifyApi
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.domain.repository.AuthRepo
import de.janaja.playlistpurger.domain.repository.TokenRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class SpotifyAuthRepo(
    private val tokenRepo: TokenRepo,
) : AuthRepo {
    private val TAG = "AuthRepo"

    private val clientSecret = BuildConfig.CLIENT_SECRET
    private val clientId = BuildConfig.CLIENT_ID
    private val redirectUri = "asdf://callback"

    private val api = SpotifyAccountApi.retrofitService
    private val webApi = SpotifyApi.retrofitService

    private val accessTokenFlow = tokenRepo.accessTokenFlow

    private val refreshTokenFlow = tokenRepo.refreshTokenFlow

    override val loginState = accessTokenFlow
        .map { checkToken(it) }


    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun loginWithCode(code: String) {
        try {

            val tokenRequestResponse = api.getToken(
                client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                code = code,
                redirectUri = redirectUri,
            )

            tokenRepo.updateAccessToken(tokenRequestResponse.accessToken)
            tokenRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
        } catch (e: Exception) {
            Log.d(TAG, "getTokenForCode: Error ${e.localizedMessage}")
        }
    }

    private suspend fun checkToken(token: String?): LoginState {
        Log.d(TAG, "received token: $token")

        if (token == null) {
            Log.d(TAG, "no saved token -> logged out")
            return LoginState.LoggedOut
        } else {
            Log.d(TAG, "found saved token -> check if valid by loading current user")
            val response = webApi.getCurrentUser("Bearer $token")

            if (response.isSuccessful) {
                Log.d(TAG, "token is valid -> logged in")
                val userId = response.body()!!.id
                // TODO maybe whole user
                return LoginState.LoggedIn(userId)
            } else {
                Log.d(TAG, "token is not valid")
                when (response.code()) {
                    401 -> {
                        Log.d(TAG, "try to refresh token")

                        if (!refreshToken()) {
                            return LoginState.LoggedOut
                        }
                    }
                }
                // TODO improve error handling and think about loadingState flows in exception case (endless refresh/reload or loadingState?)
                return LoginState.Loading
            }
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
            try {
                // TODO improve error handling
                Log.d(TAG, "found saved refresh token -> get fresh access token with it")
                val tokenRequestResponse = api.refreshToken(
                    client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                    refreshToken = value,
                )
                Log.d(TAG, "got fresh access token -> save it")
                tokenRepo.updateAccessToken(tokenRequestResponse.accessToken)
                if (tokenRequestResponse.refreshToken != "") {
                    tokenRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
                }
                return true
            } catch (e: Exception) {
                Log.e(TAG, "refreshToken: Error: ${e.localizedMessage}")
                return false
            }

        }
    }


    override suspend fun logout() {
        tokenRepo.deleteAllToken()
    }
}