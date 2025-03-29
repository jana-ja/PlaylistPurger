package de.janaja.playlistpurger.ui.viewmodel

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import com.spotify.sdk.android.auth.AuthorizationClient
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

    fun startLoginProcess() {
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

    fun handleLoginResult(result: ActivityResult) {
        // Check if result comes from the correct activity
        if (result.resultCode == RESULT_OK && result.data != null) {
            // apparently don't need to check with request code if ActivityResult comes from spotify's login activity
//                    val resultCode = result.resultCode
//                    val data = result.data
            val response: AuthorizationResponse =
                AuthorizationClient.getResponse(result.resultCode, result.data)
//                    result
            when (response.type) {
                // Response was successful and contains auth token
                AuthorizationResponse.Type.TOKEN -> {
                    println("Success! ${AuthorizationResponse.Type.TOKEN}")
                }
                // Auth flow returned an error
                AuthorizationResponse.Type.ERROR -> {
                    println("Error")
                    println(AuthorizationResponse.Type.ERROR)
                }
                // Most likely auth flow was cancelled
                else -> {
                    println("Auth flow canceled")
                }
            }
        } else {
            println("No result returned")
        }
    }
}