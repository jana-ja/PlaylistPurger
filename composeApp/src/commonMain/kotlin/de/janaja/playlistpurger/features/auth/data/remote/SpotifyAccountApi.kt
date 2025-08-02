package de.janaja.playlistpurger.features.auth.data.remote

import de.janaja.playlistpurger.features.auth.data.model.TokenRequestResponseDto
/*
likely unnecessary, but i'll keep it for potential mocking/testing in the future
 */
// could also be SpotifyAccountClient, but the implementation actually uses a HttpClient so Api fits even better
interface SpotifyAccountApi {
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