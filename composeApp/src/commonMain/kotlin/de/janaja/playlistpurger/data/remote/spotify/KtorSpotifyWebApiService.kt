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

class KtorSpotifyWebApiService (
    private val httpClient: HttpClient
) : SpotifyWebApiService {

    private val baseUrl = "https://api.spotify.com/v1/"

    override suspend fun getCurrentUsersPlaylists(
        token: String
    ): Result<PlaylistResponseDto> {
        return safeCall {
            httpClient.get(baseUrl + "me/playlists") {
                header("Authorization", token)
                // TODO test bearerAuth instead of header
            }
        }
    }

    override suspend fun getTracksForPlaylist(
        token: String,
        playlistId: String,
    ): Result<TracksResponseDto> {
        return safeCall {
            httpClient.get(baseUrl + "playlists/${playlistId}/tracks") {
                header("Authorization", token)
            }
        }
    }

    override suspend fun getCurrentUser(
        token: String,
    ): Result<UserDto> {
        return safeCall<UserDto> {
            httpClient.get(baseUrl + "me") {
                header("Authorization", token)
            }
        }
    }
}