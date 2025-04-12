package de.janaja.playlistpurger.data.repository

import android.util.Log
import de.janaja.playlistpurger.BuildConfig
import de.janaja.playlistpurger.data.remote.spotify.SpotifyAccountApi
import de.janaja.playlistpurger.data.remote.spotify.SpotifyApi
import de.janaja.playlistpurger.domain.repository.AuthRepo
import de.janaja.playlistpurger.domain.repository.TokenRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
/*
vllt loginstate: LOGGED_IN(userId), LOGGED_OUT, LOADING
A:
  - ich habe ein token
  - ich will prüfen ob es gültig ist
  - ich sende request an api und sehe ob es gültig ist (könnte dabei zB auch userid holen)
  - falls 401 refresh, sonst nicht (grundsätzliche kopplung)

B:
  - ich habe ein token
  - ich will prüfen ob es gültig ist
  - ich schaue die abgespeicherte zeit an und rechne ob es noch gültig ist
  - refresh falls nur noch <10min oder so gültig, sonst nicht

 */

sealed class LoginState() {
    data class LoggedIn(val userId: String): LoginState()
    data object LoggedOut: LoginState()
    data object Loading: LoginState()
}
class SpotifyAuthRepo(
    private val tokenRepo: TokenRepo,
) : AuthRepo {
    private val TAG = "AuthRepo"

    private val clientSecret = BuildConfig.CLIENT_SECRET
    private val clientId = BuildConfig.CLIENT_ID
    private val redirectUri = "asdf://callback"

    private val api = SpotifyAccountApi.retrofitService
    private val webApi = SpotifyApi.retrofitService

    // FLOW emittet nur wenn es consumer gibt!
    private val accessTokenFlow = tokenRepo.accessTokenFlow
        .onEach { checkToken(it) }
    // TODO connect loginState to this flow, viewmodel collects loginstate so flow can emit and flow


    // hier könnte man direkt noch onEach oder map dran hängen
    private val refreshTokenFlow = tokenRepo.refreshTokenFlow

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    override val loginState = _loginState.asStateFlow()

//    private val _isLoggedIn = MutableStateFlow(false)
//    override val isLoggedIn: Flow<Boolean> = _isLoggedIn.asStateFlow()




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

//    private suspend fun checkUserLoggedIn() {
//        accessTokenFlow.collect { value ->
//            Log.d(TAG, "token collect: $value")
//
//            if (value == null) {
//                Log.d(TAG, "did not find token in data store")
//                _loginState.value = LoginState.LoggedOut
////                _isLoggedIn.value = false
//            } else {
//                Log.d(TAG, "found token in data store")
//                checkToken(it)
//                // TODO erst check
//                _isLoggedIn.value = true
//            }
//            Log.d(TAG, "token: $value")
//        }
//    }

    private suspend fun checkToken(token: String?) {
        Log.d(TAG, "received token: $token")

        if (token == null) {
            Log.d(TAG, "no saved token -> logged out")
            _loginState.value = LoginState.LoggedOut
//                _isLoggedIn.value = false
        } else {
            Log.d(TAG, "found saved token -> check if valid")

            val response = webApi.getCurrentUser(token)
            if (response.isSuccessful) {
                Log.d(TAG, "token is valid -> logged in")
                val userId = response.body()!!.id
                // TODO maybe whole user
                _loginState.value = LoginState.LoggedIn(userId)
            } else {
                when (response.code()) {
                    401 -> refreshToken()
                }
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun refreshToken() {
        // unsuccessful -> set loginState to logged out
        // successful -> update access token will trigger check token and set the loginState

        val value = refreshTokenFlow.first()

        Log.d(TAG, "refresh token collect: $value")
        Log.d(TAG, "try receive refresh token from data store")

        if (value == null) {
            Log.d(TAG, "did not find refresh token in data store")
            logout()
//            _isLoggedIn.value = false
        } else {
            Log.d(TAG, "found refresh token in data store")
            try {


                val tokenRequestResponse = api.refreshToken(

                    client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                    refreshToken = value,
                )
                tokenRepo.updateAccessToken(tokenRequestResponse.accessToken)
                if (tokenRequestResponse.refreshToken != "") {
                    tokenRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
                }
            } catch (e: Exception) {
                Log.e(TAG, "refreshToken: Error: ${e.localizedMessage}")
            }

        }
        Log.d(TAG, "token: $value")

    }


    override suspend fun logout() {
        tokenRepo.deleteAllToken()
        _loginState.value = LoginState.LoggedOut
//        _isLoggedIn.value = false
    }
}