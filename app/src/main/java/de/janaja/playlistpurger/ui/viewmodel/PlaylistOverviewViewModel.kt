package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.domain.exception.DataException
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.repository.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlaylistOverviewViewModel(
    private val authService: AuthService,
    private val playListRepo: PlaylistRepo
) : ViewModel() {

    private val TAG = "PlaylistOverviewViewModel"

    val playlists = MutableStateFlow<List<Playlist>>(listOf())

    init {
        loadAllPlaylists()
    }

    /*
    Token refreshing is part of the application's business logic, not just a data access detail.
    If a token refresh fails, the ViewModel might need to update the UI to reflect this.
     */
    fun loadAllPlaylists() {
        viewModelScope.launch {

            val result = playListRepo.getPlaylists()

            result.onSuccess { allPlaylists ->
                Log.d(TAG, "loadAllPlaylists: success")
                playlists.value = allPlaylists

            }.onFailure { e ->
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
                // look for authorization error, refresh token, retry
                when (e) {
                    DataException.Remote.InvalidAccessToken -> {
                        authService.refreshToken()
                        // TODO try again
                    }
                    is DataException -> {
                        // TODO exception handling, show error message etc
                    }
                    else -> {

                    }

                }
//                if (e is DataException.Remote) {
//
//                    when (e) {
//                        DataException.Remote.InvalidAccessToken -> {
//                            authRepo.refreshToken()
//                        }
//                        DataException.Remote.MissingAccessToken -> TODO()
//                        DataException.Remote.NoInternet -> TODO()
//                        DataException.Remote.RequestTimeout -> TODO()
//                        DataException.Remote.Serialization -> TODO()
//                        DataException.Remote.Server -> TODO()
//                        DataException.Remote.TooManyRequests -> TODO()
//                        DataException.Remote.Unknown -> TODO()
//                    }
//                } else {
//                    // other exception
//                }
            }

        }
    }
}