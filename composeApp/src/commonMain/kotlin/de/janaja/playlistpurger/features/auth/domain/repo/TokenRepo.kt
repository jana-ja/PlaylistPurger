package de.janaja.playlistpurger.features.auth.domain.repo

import kotlinx.coroutines.flow.Flow

interface TokenRepo {
    val accessTokenFlow: Flow<String?>
    val refreshTokenFlow: Flow<String?>
    suspend fun updateAccessToken(token: String)
    suspend fun updateRefreshToken(token: String)
    suspend fun deleteAllToken()
}