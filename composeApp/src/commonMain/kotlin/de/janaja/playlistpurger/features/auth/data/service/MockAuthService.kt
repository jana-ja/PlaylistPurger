package de.janaja.playlistpurger.features.auth.data.service

import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow

class MockAuthService(
    private val isSuccessFul: Boolean
) : AuthService {

    override val userLoginState = MutableStateFlow<UserLoginState>(UserLoginState.LoggedIn)//(UserDetails.Full("0", "Test User", "https://i.scdn.co/image/ab67757000003b82f63072e4fad4e5170c1fda52")))

    override suspend fun loginWithCode(code: String): Result<Unit> {
        return if (isSuccessFul) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("some exception"))
        }
    }

    override fun getAuthenticationUrl(): String {
        return ""
    }

    override suspend fun logout() {

    }
}