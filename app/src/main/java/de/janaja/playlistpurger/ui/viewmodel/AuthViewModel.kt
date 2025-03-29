package de.janaja.playlistpurger.ui.viewmodel

import android.app.Activity.RESULT_OK
import android.app.Application
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import de.janaja.playlistpurger.DatastoreKeys
import de.janaja.playlistpurger.util.DataStorePreferences
import de.janaja.playlistpurger.util.SecurityUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel(
    application: Application,
    val onStartLoginActivity: (AuthorizationRequest) -> Unit,
) : AndroidViewModel(application) {

    private val TAG = "AuthViewModel"

    private val dataStorePreferences = DataStorePreferences(
        application,
        SecurityUtil()
    )

    private val CLIENT_ID = "1f7401f5d27847b99a6dfe6908c5ccac"
    private val REDIRECT_URI = "asdf://callback"

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val tokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.accessToken)
//    val tokenStateFlow = tokenFlow
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = ""
//        )

    init {
        checkAuth()
    }

    fun checkAuth() {
        viewModelScope.launch {
        // do I have a saved token?
            tokenFlow.collect { value ->
                if (value == null) {
                    Log.d(TAG, ": did not find token")


                } else {
                    Log.d(TAG, ": found token")
                    checkToken()
                }
                Log.d(TAG, "token: $value")
                _isLoading.value = false
            }
        }


        // ist das token noch gültig?

        // rein in die app
        _isLoggedIn.value = true
    }

    private fun checkToken() {

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
                        dataStorePreferences.putSecurePreference(DatastoreKeys.accessToken, response.accessToken)
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
    }

    /*
    private val dataStore = application.dataStore

    private val nameFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[DatastoreKeys.userName] ?: ""
        }

    val nameStateFlow = nameFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ""
        )

        fun updateName(name: String) {
        viewModelScope.launch {
            dataStore.edit { values ->
                values[DatastoreKeys.userName] = name
            }
        }
    }
     */



}