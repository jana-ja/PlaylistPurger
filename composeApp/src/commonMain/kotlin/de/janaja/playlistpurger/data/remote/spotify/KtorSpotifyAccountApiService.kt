package de.janaja.playlistpurger.data.remote.spotify

import de.janaja.playlistpurger.data.remote.safeCall
import de.janaja.playlistpurger.data.remote.spotify.model.TokenRequestResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorSpotifyAccountApiService (
    private val httpClient: HttpClient
): SpotifyAccountApiService {

    private val baseUrl = "https://accounts.spotify.com/"

    override suspend fun getToken(
        client: String,
        code: String,
        redirectUri: String,
        ): Result<TokenRequestResponseDto> {

        return safeCall {
            httpClient.submitForm(
                url = baseUrl + "api/token",
                formParameters = parameters {
                    append("code", code)
                    append("redirect_uri", redirectUri)
                    append("grant_type", "authorization_code")
                }
            ) {
                headers {
                    append("Authorization", client)
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
            }
        }

    }

    override suspend fun refreshToken(
        client: String,
        refreshToken: String,
        ): Result<TokenRequestResponseDto> {

        return safeCall {
            httpClient.submitForm(
                url = baseUrl + "api/token",
                formParameters = parameters {
                    append("refresh_token", refreshToken)
                    append("grant_type", "refresh_token")
                }
            ) {
                headers {
                    append("Authorization", client)
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
            }
        }
    }
}