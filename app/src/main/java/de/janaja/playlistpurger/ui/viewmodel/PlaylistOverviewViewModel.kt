package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.remote.SpotifyApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class PlaylistOverviewViewModel() : ViewModel() {
    val TAG = "DasViewModel"

    val api = SpotifyApi.retrofitService
    var token = ""

    val playlists = MutableStateFlow<List<Playlist>>(listOf())

    init {
//        loadAllPlaylists()
    }

    fun loadAllPlaylists() {
        viewModelScope.launch {

            try {
                val budf = api.getCatImagesWithHeader("Bearer " + token)
                playlists.value = budf.items
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
//                Log.e(TAG, "loadAllPlaylists: ${e.stackTrace.}")
            }

        }
    }

    fun updateToken(newToken: String) {
        token = newToken
    }
}