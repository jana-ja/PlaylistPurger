package de.janaja.playlistpurger.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.DatastoreKeys
import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.remote.SpotifyApi
import de.janaja.playlistpurger.util.DataStorePreferences
import de.janaja.playlistpurger.util.SecurityUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class PlaylistOverviewViewModel(application: Application) : AndroidViewModel(application) {
    val TAG = "DasViewModel"

    val api = SpotifyApi.retrofitService


    private val dataStorePreferences = DataStorePreferences(
        application,
        SecurityUtil()
    )
    // TODO überall aus datastore holen und den injecten oder das token injecten oder was?
    // überlegung für token kopie mitgeben: wie funktioniert der flow beim abmelden?
    private val tokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.accessToken)

    val playlists = MutableStateFlow<List<Playlist>>(listOf())

    lateinit var token: String

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
                    loadAllPlaylists()
                }
            }
        }
//        loadAllPlaylists()
    }

    fun loadAllPlaylists() {
        viewModelScope.launch {

            try {
                val budf = api.getCatImagesWithHeader("Bearer " + token)
                playlists.value = budf.items

                // TODO response check und auf 401 reagieren
                var blub = ""
                budf.items.forEach { blub += "\n\t$it" }

                Log.d(TAG, "loadAllPlaylists: $blub")
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
//                Log.e(TAG, "loadAllPlaylists: ${e.stackTrace.}")
            }

        }
    }

}