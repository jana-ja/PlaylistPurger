package de.janaja.playlistpurger.features.playlist_overview.data.repo

import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.core.util.ConcurrentLruCache
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.features.playlist_overview.data.model.toPlaylist
import de.janaja.playlistpurger.shared.data.model.toUserDetails
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.collections.set

class SpotifyPlaylistRepo(
    private val webApi: SpotifyWebApi,
    private val userRepo: UserRepo
) : PlaylistRepo {

    companion object {
        private const val TAG = "PlaylistRepo"
    }

    // TODO handle pagination or more then 50 playlists
    private val playlistsCache: MutableMap<String, List<Playlist>> =
        ConcurrentLruCache(50)

    override suspend fun getPlaylists(): Result<List<Playlist>> {
        return withContext(Dispatchers.IO) {

            playlistsCache["all_playlists"]?.let { cachedPlaylists ->
                Log.d(TAG, "getPlaylists: found playlists in cache")
                return@withContext Result.success(cachedPlaylists)
            }

            Log.d(TAG, "getPlaylists: did not find playlists in cache. try loading from api")

            val result = webApi.getCurrentUsersPlaylists()

            return@withContext result.fold(
                onSuccess = { playlistResponse ->

                    val uniqueUserIds = playlistResponse.items.map { it.owner.id }.distinct()

                    val userMap = uniqueUserIds.associateWith {
                        userRepo.getUserForId(it).fold(
                            onSuccess = { user -> user },
                            onFailure = { null }
                        )
                    }

                    val mergedPlaylists = playlistResponse.items.map {
                        val userDto = it.owner
                        val userDetails = userMap[it.owner.id] ?: userDto.toUserDetails()
                        it.toPlaylist(userDetails)
                    }

                    playlistsCache["all_playlists"] = mergedPlaylists
                    Result.success(mergedPlaylists)
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }
    }
}