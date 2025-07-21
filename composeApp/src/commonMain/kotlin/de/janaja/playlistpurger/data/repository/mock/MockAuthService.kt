package de.janaja.playlistpurger.data.repository.mock

import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.domain.model.User
import de.janaja.playlistpurger.domain.repository.AuthService
import kotlinx.coroutines.flow.MutableStateFlow

class MockAuthService(
    private val isSuccessFul: Boolean
) : AuthService {

    override val accessToken = MutableStateFlow("access token")

    override val loginState = MutableStateFlow(LoginState.LoggedIn(User("0", "Test User")))

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