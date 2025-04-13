package de.janaja.playlistpurger.ui.viewmodel

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import de.janaja.playlistpurger.BuildConfig
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.ui.UiText
import de.janaja.playlistpurger.ui.handleDataException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authService: AuthService,
    private val onStartLoginActivity: (AuthorizationRequest) -> Unit,
) : ViewModel() {

    private val clientId = BuildConfig.CLIENT_ID
    private val redirectUri = "asdf://callback"

    private val TAG = "AuthViewModel"


    val loginState = authService.loginState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = LoginState.Loading
        )

    private val _errorMessage = MutableStateFlow<UiText?>(null)
    val errorMessage = _errorMessage.asStateFlow()


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

    fun handleLoginResult(activityResult: ActivityResult) {
        // Check if result comes from the correct activity
        if (activityResult.resultCode == RESULT_OK && activityResult.data != null) {
            // apparently don't need to check with request code if ActivityResult comes from spotify's login activity
//                    val resultCode = result.resultCode
//                    val data = result.data
            val response: AuthorizationResponse =
                AuthorizationClient.getResponse(activityResult.resultCode, activityResult.data)
//                    result
            when (response.type) {
                // Response was successful and contains auth token
                AuthorizationResponse.Type.CODE -> {
                    // get accessToken and refreshToken from api with code
                    println("Got Token! ${AuthorizationResponse.Type.TOKEN}")

                    viewModelScope.launch {
                        val loginResult = authService.loginWithCode(response.code)
                        loginResult.onFailure { e ->
                            Log.e(TAG, "handleLoginResult: ", e.cause)
                            handleDataException(
                                e = e,
                                onRefresh = {
                                    viewModelScope.launch {
                                        authService.refreshToken()
                                    }
                                },
                                onLogout = {
                                    viewModelScope.launch {
                                        authService.logout()
                                    }
                                },
                                onUpdateErrorMessage = { _errorMessage.value = it }
                            )
                        }
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
