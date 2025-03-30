package de.janaja.playlistpurger.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {
    val accessTokenFlow: Flow<String?>
    val refreshTokenFlow: Flow<String?>
//    val userIdFlow: Flow<String?>
    suspend fun updateAccessToken(token: String)
    suspend fun updateRefreshToken(token: String)
    suspend fun deleteAllToken()
}