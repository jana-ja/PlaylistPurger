package de.janaja.playlistpurger.ui.viewmodel

import android.app.Activity.RESULT_OK
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel(
    val dataStoreRepo: DataStoreRepo,
    val onStartLoginActivity: (AuthorizationRequest) -> Unit,
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val CLIENT_ID = "1f7401f5d27847b99a6dfe6908c5ccac"
    private val REDIRECT_URI = "asdf://callback"

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // FLOW emittet nur wenn es consumer gibt!
    private val tokenFlow = dataStoreRepo.tokenFlow
    // hier könnte man direkt noch onEach oder map dran hängen

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            tokenFlow.collect { value ->
                Log.d(TAG, "token collect: $value")
                Log.d(TAG, "try receive token from data store")

                if (value == null) {
                    Log.d(TAG, "did not find token in data store")
                    _isLoggedIn.value = false
                } else {
                    Log.d(TAG, "found token in data store")
                    checkToken()
                    // TODO erst check
                    _isLoggedIn.value = true
                }
                Log.d(TAG, "token: $value")
                _isLoading.value = false
            }
        }
    }



    private fun checkToken() {
        // TODO implement
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
                AuthorizationResponse.Type.TOKEN -> {
                    viewModelScope.launch {
                        dataStoreRepo.updateToken(
                            response.accessToken
                        )
                        println("Success! ${AuthorizationResponse.Type.TOKEN}")
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

    fun logout() {
        // delete token
        viewModelScope.launch {
            dataStoreRepo.deleteToken()
            _isLoggedIn.value = false
        }
    }
}