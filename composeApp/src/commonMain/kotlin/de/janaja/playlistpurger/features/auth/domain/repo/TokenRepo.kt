package de.janaja.playlistpurger.features.auth.domain.repo

import kotlinx.coroutines.flow.Flow

// TODO maybe remove from domain
interface TokenRepo {
    val accessTokenFlow: Flow<String?>
    val refreshTokenFlow: Flow<String?>
//    val userIdFlow: Flow<String?>
    suspend fun updateAccessToken(token: String)
    suspend fun updateRefreshToken(token: String)
    suspend fun deleteAllToken()
}