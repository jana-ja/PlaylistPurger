package de.janaja.playlistpurger.ui.viewmodel

import android.app.Activity.RESULT_OK
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import de.janaja.playlistpurger.BuildConfig
import de.janaja.playlistpurger.data.repository.LoginState
import de.janaja.playlistpurger.domain.repository.AuthRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authRepo: AuthRepo,
    private val onStartLoginActivity: (AuthorizationRequest) -> Unit,
) : ViewModel() {

    private val clientId = BuildConfig.CLIENT_ID
    private val redirectUri = "asdf://callback"

    private val TAG = "AuthViewModel"


    val loginState = authRepo.loginState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = LoginState.Loading
        )
//    val isLoggedIn = authRepo.isLoggedIn
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = false
//        )

    // ui states
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading = _isLoading.asStateFlow()



    /*
    save validUntil time for access token
    init: check validUntil time ONCE
        -> null: loggedin false
        -> not valid: refresh
        -> valid: loggedin true (access token sollte dann da sein, wird nicht explizit geprüft?
    flow accesstoken:
        null -> loggedout
        value -> loggedin

    oder:

    init: check
     */


    fun startLoginProcess() {
        val builder = AuthorizationRequest.Builder(
            clientId,
            AuthorizationResponse.Type.CODE,
            redirectUri
        )
        builder.setScopes(arrayOf("playlist-read-private"))
        builder.setShowDialog(true)

        val request: AuthorizationRequest = builder.build()

        onStartLoginActivity(request)
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
                AuthorizationResponse.Type.CODE -> {
                    // get accessToken and refreshToken from api with code
                    println("Got Token! ${AuthorizationResponse.Type.TOKEN}")

                    viewModelScope.launch {
                        authRepo.loginWithCode(response.code)
                    }


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