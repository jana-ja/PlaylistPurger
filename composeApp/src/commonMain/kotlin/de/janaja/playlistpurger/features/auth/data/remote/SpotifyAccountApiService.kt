package de.janaja.playlistpurger.features.auth.data.remote

import de.janaja.playlistpurger.features.auth.data.model.TokenRequestResponseDto
/*
likely unnecessary, but i'll keep it for potential mocking/testing in the future
 */
interface SpotifyAccountApiService {
    suspend fun getToken(
        client: String,
        code: String,
        redirectUri: String,
    ): Result<TokenRequestResponseDto>

    suspend fun refreshToken(
        client: String,
        refreshToken: String,
    ): Result<TokenRequestResponseDto>
}