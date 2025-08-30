package de.janaja.playlistpurger.features.auth.domain.service

import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val userLoginState: Flow<UserLoginState>
    val accessToken: StateFlow<TokenState>
    suspend fun refreshTokenOrLogout(): Result<Unit>
    suspend fun logout()
    suspend fun loginWithCode(code: String): Result<Unit>
    fun getAuthenticationUrl(): String
}