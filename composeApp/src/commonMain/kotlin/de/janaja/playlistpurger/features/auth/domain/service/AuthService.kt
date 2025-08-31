package de.janaja.playlistpurger.features.auth.domain.service

import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val userLoginState: Flow<UserLoginState>
    suspend fun logout()
    suspend fun loginWithCode(code: String): Result<Unit>
    fun getAuthenticationUrl(): String
}