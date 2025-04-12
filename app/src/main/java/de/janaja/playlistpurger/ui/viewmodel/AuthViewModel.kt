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
import de.janaja.playlistpurger.data.remote.spotify.SpotifyAccountApi
import de.janaja.playlistpurger.domain.repository.DataStoreRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class AuthViewModel(
    private val dataStoreRepo: DataStoreRepo,
    private val onStartLoginActivity: (AuthorizationRequest) -> Unit,
) : ViewModel() {

    private val clientSecret = BuildConfig.CLIENT_SECRET
    private val clientId = BuildConfig.CLIENT_ID
    private val redirectUri = "asdf://callback"

    private val TAG = "AuthViewModel"


    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // FLOW emittet nur wenn es consumer gibt!
    private val accessTokenFlow = dataStoreRepo.accessTokenFlow

    // hier könnte man direkt noch onEach oder map dran hängen
    private val refreshTokenFlow = dataStoreRepo.refreshTokenFlow

    // TODO eig kein diekt zurgriff auf api
    private val api = SpotifyAccountApi.retrofitService

    init {
        viewModelScope.launch {
            // TODO make better
            refreshToken()
            checkUserLoggedIn()
        }
    }

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
    // umbenennen listener/observer
    private fun checkUserLoggedIn() {
        viewModelScope.launch {
            accessTokenFlow.collect { value ->
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

    private suspend fun checkToken() {
        // TODO implement
        if (false) {
//            refreshToken()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun refreshToken() {
        val value = refreshTokenFlow.first()

        Log.d(TAG, "refresh token collect: $value")
        Log.d(TAG, "try receive refresh token from data store")

        if (value == null) {
            Log.d(TAG, "did not find refresh token in data store")
            _isLoggedIn.value = false
        } else {
            Log.d(TAG, "found refresh token in data store")
            try {


                val tokenRequestResponse = api.refreshToken(

                    client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                    refreshToken = value,
                )
                dataStoreRepo.updateAccessToken(tokenRequestResponse.accessToken)
                if (tokenRequestResponse.refreshToken != "") {
                    dataStoreRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
                }
                _isLoggedIn.value = true
            } catch (e: Exception) {
                Log.e(TAG, "refreshToken: Error: ${e.localizedMessage}")
            }

        }
        Log.d(TAG, "token: $value")
        _isLoading.value = false

    }

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
                    getTokenForCode(response.code)


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
            dataStoreRepo.deleteAllToken()
            _isLoggedIn.value = false
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun getTokenForCode(code: String) {
        viewModelScope.launch {
            try {

                val tokenRequestResponse = api.getToken(
                    client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                    code = code,
                    redirectUri = redirectUri,
                )

                dataStoreRepo.updateAccessToken(tokenRequestResponse.accessToken)
                dataStoreRepo.updateRefreshToken(tokenRequestResponse.refreshToken)
            } catch (e: Exception) {
                Log.d(TAG, "getTokenForCode: Error ${e.localizedMessage}")
            }

        }
    }
}