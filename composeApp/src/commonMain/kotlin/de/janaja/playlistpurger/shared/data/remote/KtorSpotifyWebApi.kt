package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.core.data.remote.safeCall
import de.janaja.playlistpurger.features.playlist_overview.data.model.PlaylistResponseDto
import de.janaja.playlistpurger.shared.data.model.PlayRequestOffset
import de.janaja.playlistpurger.shared.data.model.PlayRequest
import de.janaja.playlistpurger.shared.data.model.TracksResponseDto
import de.janaja.playlistpurger.shared.data.model.UserFullDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

class KtorSpotifyWebApi(
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

    // https://api.spotify.com/v1/me/player/play
    override suspend fun playTrack(contextUri: String, trackUri: String): Result<Unit?> {
        return safeCall<Unit?> {
            httpClient.put("me/player/play") {
                setBody(
                    PlayRequest(contextUri, PlayRequestOffset(trackUri))
                )
            }
        }
    }

    // https://api.spotify.com/v1/me/player/pause
    override suspend fun pauseTrack(): Result<Unit?> {
        return safeCall {
            httpClient.put("me/player/pause")
        }
    }
}