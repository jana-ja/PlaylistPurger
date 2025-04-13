package de.janaja.playlistpurger.data.remote.spotify

import de.janaja.playlistpurger.data.remote.spotify.model.TokenRequestResponseDto

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