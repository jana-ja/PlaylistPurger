package de.janaja.playlistpurger.features.auth.data.remote

import de.janaja.playlistpurger.core.data.remote.safeCall
import de.janaja.playlistpurger.features.auth.data.model.TokenRequestResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.http.parameters

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