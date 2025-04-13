package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.mapper.toPlaylist
import de.janaja.playlistpurger.data.remote.spotify.SpotifyWebApiService
import de.janaja.playlistpurger.domain.exception.DataException
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.repository.TokenRepo
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import kotlinx.coroutines.flow.firstOrNull

class SpotifyPlaylistRepo(
    tokenRepo: TokenRepo,
    private val webApiService: SpotifyWebApiService
): PlaylistRepo {

    private val tokenFlow = tokenRepo.accessTokenFlow

    override suspend fun getPlaylists(): Result<List<Playlist>> {
        val token = tokenFlow.firstOrNull() ?: return Result.failure(DataException.Remote.MissingAccessToken)

        val result = webApiService.getCurrentUsersPlaylists("Bearer $token")

        result.onSuccess { playlistResponse ->
            return Result.success(playlistResponse.items.map { it.toPlaylist() })

        }.onFailure {
            return Result.failure(it)
        }
//            .onFailure {
//            return Result.failure(Exception("something"))
//
//        }
        // TODO
            return Result.failure(Exception("something"))


        /*
        return try {
                // ... API call ...
                if (/* success */) {
                    Result.success(playlist)
                } else {
                    Result.failure(PlaylistException.Remote.Server)
                }
            } catch (e: IOException) {
                Result.failure(PlaylistException.Remote.NoInternet)
            } catch (e: SerializationException) {
                Result.failure(PlaylistException.Remote.Serialization)
            } catch (e: Exception) {
                Result.failure(PlaylistException.Remote.Unknown)
            }
         */

    }

}
/*
val result = withTokenRefresh(authRepo) {
                playlistRepo.loadPlaylist()
            }
 */

//suspend fun <T> withTokenRefresh(
//    authRepo: AuthRepo,
//    block: suspend () -> Result<T>
//): Result<T> {
//    val result = block()
//    return if (result.isFailure && result.exceptionOrNull() is DataException.Remote.InvalidAccessToken) {
//        val refreshResult = authRepo.refreshToken()
//        if (refreshResult.isSuccess) {
//            block() // Retry the original operation
//        } else {
//            Result.failure(refreshResult.exceptionOrNull() ?: Exception("Token refresh failed"))
//        }
//    } else {
//        result
//    }
//}