package de.janaja.playlistpurger.features.auth.data.service

import de.janaja.playlistpurger.features.auth.domain.model.LoginState
import de.janaja.playlistpurger.shared.domain.model.User
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow

class MockAuthService(
    private val isSuccessFul: Boolean
) : AuthService {

    override val accessToken = MutableStateFlow("access token")

    override val loginState = MutableStateFlow(LoginState.LoggedIn(User("0", "Test User", "https://i.scdn.co/image/ab67757000003b82f63072e4fad4e5170c1fda52")))

    override suspend fun loginWithCode(code: String): Result<Unit> {
        return if (isSuccessFul) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("some exception"))
        }
    }

    override suspend fun refreshToken(): Boolean {
        return isSuccessFul
    }

    override suspend fun logout() {

    }
}