package de.janaja.playlistpurger.data.remote.spotify

import de.janaja.playlistpurger.data.remote.safeCall
import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistResponseDto
import de.janaja.playlistpurger.data.remote.spotify.model.TracksResponseDto
import de.janaja.playlistpurger.data.remote.spotify.model.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorAccountApiService (
    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.BODY
        }
    }
) {

    private val baseUrl = "https://accounts.spotify.com/"

    suspend fun getCurrentUsersPlaylists(
        token: String
    ): Result<PlaylistResponseDto> {
        return safeCall {
            httpClient.get(baseUrl + "me/playlists") {
                header("Authorization", token)
                // TODO test bearerAuth instead of header
            }
        }
    }

    suspend fun getTracksForPlaylist(
        token: String,
        playlistId: String,
    ): Result<TracksResponseDto> {
        return safeCall {
            httpClient.get(baseUrl + "playlists/${playlistId}/tracks") {
                header("Authorization", token)
            }
        }
    }

    suspend fun getCurrentUser(
        token: String,
    ): Result<UserDto> {
        return safeCall<UserDto> {
            httpClient.get(baseUrl + "me") {
                header("Authorization", token)
            }
        }
    }
}