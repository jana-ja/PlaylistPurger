package de.janaja.playlistpurger.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.DatastoreKeys
import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.remote.SpotifyApi
import de.janaja.playlistpurger.ui.TrackListRoute
import de.janaja.playlistpurger.util.DataStorePreferences
import de.janaja.playlistpurger.util.SecurityUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TrackListViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {
    val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId


    val api = SpotifyApi.retrofitService

    private val dataStorePreferences = DataStorePreferences(
        application,
        SecurityUtil()
    )

    private val tokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.accessToken)

    val trackList = MutableStateFlow<List<Track>>(listOf())

    private lateinit var token: String

    init {
        viewModelScope.launch {
            // do I have a saved token?
            tokenFlow.collect { value ->
                if (value == null) {
                    Log.d(TAG, ": did not find token")
                    // TODO error hilfe stop, zurück zu login view?

                } else {
                    Log.d(TAG, ": found token")
                    token = value
                    loadTrackList()
                }
            }
        }
    }

    fun loadTrackList() {
        viewModelScope.launch {

            try {
                val budf = api.getTracksForPlaylist("Bearer " + token, playlistId)
                trackList.value = budf.items.map { it.track }

                // TODO response check und auf 401 reagieren
                Log.d(TAG, "loadAllPlaylists: success")
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
//                Log.e(TAG, "loadAllPlaylists: ${e.stackTrace.}")
            }

        }
    }

}
