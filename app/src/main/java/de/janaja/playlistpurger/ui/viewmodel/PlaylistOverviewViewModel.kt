package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.ui.DataState
import de.janaja.playlistpurger.ui.UiText
import de.janaja.playlistpurger.ui.handleDataException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaylistOverviewViewModel(
    private val authService: AuthService,
    private val playListRepo: PlaylistRepo
) : ViewModel() {

    private val TAG = "PlaylistOverviewViewModel"

    private val _dataState =
        MutableStateFlow<DataState<List<Playlist>>>(DataState.Loading)
    val dataState = _dataState.asStateFlow()

    init {
        loadAllPlaylists()
    }

    /*
    Token refreshing is part of the application's business logic, not just a data access detail.
    If a token refresh fails, the ViewModel might need to update the UI to reflect this.
    Aber ich zeige beim refreshen noch nichts an und wenn refresh fehlschlägt wird ein anderer fehler geworfen?
     */
    private fun loadAllPlaylists() {
        viewModelScope.launch {

            val result = playListRepo.getPlaylists()

            result.onSuccess { allPlaylists ->
                Log.d(TAG, "loadAllPlaylists: success")
                _dataState.value = DataState.Ready(allPlaylists)

            }.onFailure { e ->
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
                // look for authorization error, refresh token, retry
                handleDataException(
                    e = e,
                    onRefresh = {
                        viewModelScope.launch {
                            authService.refreshToken()
                        }
                    },
                    onLogout = {
                        viewModelScope.launch {
                            authService.logout()
                        }
                    },
                    onUpdateErrorMessage = {
                        _dataState.value = DataState.Error(it)
                    }
                )
            }

        }
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