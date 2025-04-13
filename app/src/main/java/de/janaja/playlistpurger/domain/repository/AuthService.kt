package de.janaja.playlistpurger.domain.repository

import de.janaja.playlistpurger.domain.model.LoginState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val loginState: Flow<LoginState>
    val accessToken: Flow<String?>
    suspend fun refreshToken(): Boolean
    suspend fun logout()
    suspend fun loginWithCode(code: String)
}