package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.core.data.remote.safeCall
import de.janaja.playlistpurger.shared.data.model.VoteDto
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MyVoteApi(
    private val httpClient: HttpClient
): VoteApi {

//    private val baseUrl = "http://10.0.2.2:8080/api/v1/" // in manifest: android:usesCleartextTraffic="true"
    private val baseUrl = "http://217.154.117.3:8080/api/v1/"

    override suspend fun getUsersVotesForPlaylist(
        playlistId: String,
        userId: String
    ): Result<List<VoteDto>> {
        return safeCall<List<VoteDto>> {
            httpClient.get(baseUrl + "votes") {
                parameter("playlistId", playlistId)
                parameter("userId", userId)
            }
        }
    }

    override suspend fun getAllVotesForPlaylist(playlistId: String): Result<List<VoteDto>> {
        return safeCall<List<VoteDto>> {
            httpClient.get(baseUrl + "votes") {
                parameter("playlistId", playlistId)
            }
        }
    }

    override suspend fun upsertVote(
        playlistId: String,
        trackId: String,
        userId: String,
        newVote: VoteOption
    ): Result<Unit> {
        return safeCall<Unit> {
            httpClient.post(baseUrl + "votes") {
                contentType(ContentType.Application.Json)
                setBody(VoteDto(playlistId, trackId, userId, newVote))
            }
        }
    }
}