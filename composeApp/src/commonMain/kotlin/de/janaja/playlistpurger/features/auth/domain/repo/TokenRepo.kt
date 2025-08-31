package de.janaja.playlistpurger.features.auth.domain.repo

import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import kotlinx.coroutines.flow.StateFlow

interface TokenRepo {
    val accessTokenFlow: StateFlow<TokenState>
    val refreshTokenFlow: StateFlow<TokenState>
    suspend fun exchangeAuthCodeForTokens(code: String): Result<Unit>
    suspend fun refreshAccessToken(): Result<String>
    suspend fun clearAllTokens()
}