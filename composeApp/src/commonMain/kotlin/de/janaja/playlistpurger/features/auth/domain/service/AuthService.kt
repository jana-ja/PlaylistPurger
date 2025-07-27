package de.janaja.playlistpurger.features.auth.domain.service

import de.janaja.playlistpurger.features.auth.domain.model.LoginState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val loginState: Flow<LoginState>
    val accessToken: Flow<String?>
    suspend fun refreshToken(): Result<Unit>
    suspend fun logout()
    suspend fun loginWithCode(code: String): Result<Unit>
}