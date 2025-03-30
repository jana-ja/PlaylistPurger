package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.PlayListRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class PlaylistOverviewViewModel(val dataStoreRepo: DataStoreRepo, val playListRepo: PlayListRepo) : ViewModel() {
    val TAG = "PlaylistOverviewViewModel"

    val playlists = MutableStateFlow<List<Playlist>>(listOf())


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