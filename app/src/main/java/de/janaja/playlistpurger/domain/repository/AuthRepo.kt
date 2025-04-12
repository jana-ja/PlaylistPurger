package de.janaja.playlistpurger.domain.repository

import de.janaja.playlistpurger.data.repository.LoginState
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    val loginState: Flow<LoginState>
    suspend fun refreshToken()
    suspend fun logout()
    suspend fun loginWithCode(code: String)
}