package de.janaja.playlistpurger.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    val onStartLoginActivity: (Int, AuthorizationRequest) -> Unit,
): ViewModel() {
    private val CLIENT_ID = "1f7401f5d27847b99a6dfe6908c5ccac"
    private val REQUEST_CODE = 1337
    private val REDIRECT_URI = "asdf://callback"

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    fun checkAuth(){
        // habe ich ein token?

        // ist das token noch gültig?

        // rein in die app
        _isLoggedIn.value = true
    }

    fun login() {
        val builder = AuthorizationRequest.Builder(
            CLIENT_ID,
            AuthorizationResponse.Type.TOKEN,
            REDIRECT_URI
        )
        builder.setScopes(arrayOf("playlist-read-private"))
        builder.setShowDialog(true)

        val request: AuthorizationRequest = builder.build()

        onStartLoginActivity(REQUEST_CODE, request)
    }
}