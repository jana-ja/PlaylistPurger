package de.janaja.playlistpurger.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    val accessTokenFlow: Flow<String?>
    val refreshTokenFlow: Flow<String?>
//    val userIdFlow: Flow<String?>
    suspend fun updateAccessToken(token: String)
    suspend fun updateRefreshToken(token: String)
    suspend fun deleteAllToken()
}