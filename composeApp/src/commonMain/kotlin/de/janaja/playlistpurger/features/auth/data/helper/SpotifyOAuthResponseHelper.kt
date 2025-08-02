package de.janaja.playlistpurger.features.auth.data.helper

import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.helper.OAuthResponseHelper
import de.janaja.playlistpurger.features.auth.domain.model.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Used in platform specific app entry points (MainActivity and ContentView.swift).
 * When the app is started from spotify login web page, the platform code calls this helper to process the OAuth response url and write the result to loginResult SharedFlow.
 * Common code (AuthViewModel) can observe the loginResult and act on it.
 *
 * @property scope
 */
class SpotifyOAuthResponseHelper(
    val scope: CoroutineScope
): OAuthResponseHelper {
    private val TAG = "LoginResponseHelper"

    private val _loginResult = MutableSharedFlow<LoginResult>(replay = 1)
    override val loginResult: Flow<LoginResult> = _loginResult.asSharedFlow()

    /**
     * handles result of spotify oauth flow
     * writes result to loginResult SharedFlow that can then be consumed by common logic
     *
     * @param url
     */
    override fun handleResponseUrl(url: String) {
        Log.d(TAG, "randleResponseUrl: $url")
        // should look like "asdf://callback?code=..."
        // or               "asdf://callback?error=..."

        val loginResult = getLoginResultForUrl(url)

        scope.launch {
            _loginResult.emit(loginResult)
        }
    }

    private fun getLoginResultForUrl(url: String): LoginResult {
        try {
            val queryParams = url.split("?")[1]
            val keyValue = queryParams.split("=")
            val key = keyValue[0]
            val value = keyValue[1]
            return when (key) {
                "code" -> {
                    LoginResult.SuccessLoginResult(value)
                }

                "error" -> {
                    LoginResult.ErrorLoginResult(value)
                }

                else -> {
                    LoginResult.ErrorLoginResult("unknown_key")
                }
            }
        } catch (e: Exception) {
            return LoginResult.ErrorLoginResult("invalid url or missing query param")
        }
    }
}