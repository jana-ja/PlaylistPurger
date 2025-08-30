package de.janaja.playlistpurger.features.auth.domain.repo

import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface TokenRepo {
    val accessTokenFlow: StateFlow<TokenState>
    val refreshTokenFlow: StateFlow<TokenState>
    suspend fun updateAccessToken(token: String)
    suspend fun updateRefreshToken(token: String)
    suspend fun deleteAllToken()
}