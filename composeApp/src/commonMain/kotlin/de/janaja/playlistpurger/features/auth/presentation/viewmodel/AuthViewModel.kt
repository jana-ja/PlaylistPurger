package de.janaja.playlistpurger.features.auth.presentation.viewmodel

import androidx.compose.ui.platform.UriHandler
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.model.LoginResult
import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.usecase.InitiateThirdPartyAuthUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.LoginWithCodeUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveThirdPartyAuthResultUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.ObserveUserLoginStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val initiateThirdPartyAuthUseCase: InitiateThirdPartyAuthUseCase,
    observeUserLoginStateUseCase: ObserveUserLoginStateUseCase,
    private val observeThirdPartyAuthResultUseCase: ObserveThirdPartyAuthResultUseCase,
    private val loginWithCodeUseCase: LoginWithCodeUseCase,
) : ViewModel() {

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

        val result = initiateThirdPartyAuthUseCase(uriHandler)
        result.onFailure {
            // TODO error handling
        }
    }

    private fun observeLoginResult() {
        viewModelScope.launch {
            val loginResultFlow = observeThirdPartyAuthResultUseCase()
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