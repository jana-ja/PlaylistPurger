package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.TrackListRepo
import de.janaja.playlistpurger.ui.TrackListRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*
viewmodel pro track?
    jeder trakc fragt einzeln ab wie gevoted wurde?

viewmodel für tracklist
    abfrage an server für playlist & user -> alle votes dafür bekommen
    oder abfrage mit allen track ids & user (ne playlist info muss eh dabei sein, man kann in unterschiedlichen playlists für den gleichen song unterschiedlich voten)

playlist tracks repository
    -> holt die tracks der playlist von spotify api
    -> holt für playlist und userid die votes vom server
    -> ordnet zu, vllt domain model?

    -> user votet -> wird an server gechickt, dann okay antwort abwarten und lokal updaten? neu von server abfragen vllt übertrieben?
 */

class TrackListViewModel(
    private val dataStoreRepo: DataStoreRepo,
    private val trackListRepo: TrackListRepo, savedStateHandle: SavedStateHandle
) : ViewModel() {
    val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId

    val trackList = MutableStateFlow<List<Track>>(listOf())

    init {
        loadTrackList()
    }

    private fun loadTrackList() {
        viewModelScope.launch {
            try {
                trackList.value = trackListRepo.getTracks(playlistId)

                // TODO response check und auf 401 reagieren
                Log.d(TAG, "loadAllPlaylists: success")
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
            }
        }
    }
}
