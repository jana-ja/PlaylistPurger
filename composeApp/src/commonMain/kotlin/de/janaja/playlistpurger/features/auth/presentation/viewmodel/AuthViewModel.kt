package de.janaja.playlistpurger.features.auth.presentation.viewmodel

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.model.LoginResult
import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.usecase.LoginWithCodeUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveLoginResponseResultUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveUserLoginStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    observeUserLoginStateUseCase: ObserveUserLoginStateUseCase,
    private val observeLoginResponseResultUseCase: ObserveLoginResponseResultUseCase,
    private val loginWithCodeUseCase: LoginWithCodeUseCase,
) : ViewModel() {

    private val clientId = "1f7401f5d27847b99a6dfe6908c5ccac"
    private val redirectUri = "asdf://callback"

    private val TAG = "AuthViewModel"

    val userLoginState = observeUserLoginStateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserLoginState.Loading
        )

    // TODO how to show?
    private val _errorMessage = MutableStateFlow<UiText?>(null)
    val errorMessage = _errorMessage.asStateFlow()


    init {
        observeLoginResult()
    }

    fun startLoginProcess(uriHandler: UriHandler) {

        // TODO
        val urlString = "https://accounts.spotify.com/de/authorize?" +
                "scope=playlist-read-private" +
                "&response_type=code" +
                "&redirect_uri=asdf%3A%2F%2Fcallback" +
                "&client_id=1f7401f5d27847b99a6dfe6908c5ccac" +
                "&show_dialog=true"

        uriHandler.openUri(
            uri = urlString,
        )
    }

    private fun observeLoginResult() {
        viewModelScope.launch {
            val loginResultFlow = observeLoginResponseResultUseCase()
            loginResultFlow.collect { loginResult ->
                handleLoginResult(loginResult)
            }
        }
    }

    private fun handleLoginResult(loginResult: LoginResult) {
        when (loginResult) {
            is LoginResult.ErrorLoginResult -> {
                Log.e(TAG, "error logging in: ${loginResult.errorMessage}")
            }
            is LoginResult.SuccessLoginResult -> {

                viewModelScope.launch {
                    val result = loginWithCodeUseCase(loginResult.code)
                    result.onFailure {
                        // TODO error handling
                    }
                }
            }
        }
    }
}