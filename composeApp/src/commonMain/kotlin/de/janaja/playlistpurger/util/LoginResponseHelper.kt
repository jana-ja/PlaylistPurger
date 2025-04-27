package de.janaja.playlistpurger.util

import de.janaja.playlistpurger.ui.viewmodel.LoginResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginResponseHelper(
    val scope: CoroutineScope
) {
    private val TAG = "LoginResponseHelper"

    private val _loginResult = MutableSharedFlow<LoginResult>(replay = 1)
    val loginResult: Flow<LoginResult> = _loginResult.asSharedFlow()

    fun handleResponseUrl(url: String) {
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