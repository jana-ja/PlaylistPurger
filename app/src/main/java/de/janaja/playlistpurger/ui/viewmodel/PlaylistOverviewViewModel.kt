package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistDto
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.PlaylistRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlaylistOverviewViewModel(
    private val dataStoreRepo: DataStoreRepo,
    private val playListRepo: PlaylistRepo
) : ViewModel() {

    private val TAG = "PlaylistOverviewViewModel"

    val playlists = MutableStateFlow<List<PlaylistDto>>(listOf())

    init {
        loadAllPlaylists()
    }

    fun loadAllPlaylists() {
        viewModelScope.launch {

            try {
                playlists.value = playListRepo.getPlaylists()

                // TODO response check und auf 401 reagieren
                Log.d(TAG, "loadAllPlaylists: success")
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
            }

        }
    }
}