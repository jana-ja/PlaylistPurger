package de.janaja.playlistpurger.data.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {
    val tokenFlow: Flow<String?>
//    val userIdFlow: Flow<String?>
    suspend fun updateToken(token: String)
    suspend fun deleteToken()
}