package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.core.data.remote.safeCall
import de.janaja.playlistpurger.features.playlist_overview.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserFullDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class KtorSpotifyWebApi (
    private val httpClient: HttpClient
) : SpotifyWebApi {

    override suspend fun getCurrentUsersPlaylists(): Result<PlaylistResponseDto> {
        return safeCall {
            httpClient.get("me/playlists")
        }
    }

    override suspend fun getTracksForPlaylist(playlistId: String, limit: Int, offset: Int): Result<TracksResponseDto> {
        return safeCall {
            httpClient.get("playlists/${playlistId}/tracks") {
                parameter("limit", limit)
                parameter("offset", offset)
            }
        }
    }

    override suspend fun getCurrentUser(): Result<UserFullDto> {
        return safeCall<UserFullDto> {
            httpClient.get("me")
        }
    }

    override suspend fun getUserForId(userId: String): Result<UserFullDto> {
        return safeCall<UserFullDto> {
            httpClient.get("users/$userId")
        }

    }
}