package de.janaja.playlistpurger.ui.viewmodel

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.ui.UiText
import de.janaja.playlistpurger.ui.handleDataException
import de.janaja.playlistpurger.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class LoginResult(){
    data class SuccessLoginResult(val code: String): LoginResult()
    data class ErrorLoginResult(val errorMessage: String): LoginResult()
}

class AuthViewModel(
    private val authService: AuthService,
) : ViewModel() {

    private val clientId = "1f7401f5d27847b99a6dfe6908c5ccac"
    private val redirectUri = "asdf://callback"

    private val TAG = "AuthViewModel"


    val loginState = authService.loginState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = LoginState.Loading
        )

    // TODO how to show?
    private val _errorMessage = MutableStateFlow<UiText?>(null)
    val errorMessage = _errorMessage.asStateFlow()


    fun startLoginProcess(uriHandler: UriHandler) {

        val urlString = "https://accounts.spotify.com/de/authorize?scope=playlist-read-private&response_type=code&redirect_uri=asdf%3A%2F%2Fcallback&client_id=1f7401f5d27847b99a6dfe6908c5ccac&show_dialog=true"
//    val urlString = "https://accounts.spotify.com/de/login?continue=https%3A%2F%2Faccounts.spotify.com%2Fauthorize%3Fscope%3Dplaylist-read-private%26response_type%3Dcode%26redirect_uri%3Dasdf%253A%252F%252Fcallback%26client_id%3D1f7401f5d27847b99a6dfe6908c5ccac%26show_dialog%3Dtrue"


        uriHandler.openUri(
            uri = urlString,
        )
    }

    fun handleLoginResult(loginResult: LoginResult) {
        when (loginResult) {
            is LoginResult.ErrorLoginResult -> {
                Log.e(TAG, "error logging in: ${loginResult.errorMessage}")
            }
            is LoginResult.SuccessLoginResult -> {
                viewModelScope.launch {
                    val authResult = authService.loginWithCode(loginResult.code)
                    authResult.onFailure { e ->
                        Log.e(TAG, "handleLoginResult: ", e)
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
        }
    }
}