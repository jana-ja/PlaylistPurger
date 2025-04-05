package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.TrackListRepo
import de.janaja.playlistpurger.ui.TrackListRoute
import de.janaja.playlistpurger.ui.component.SwipeDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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
    private val trackListRepo: TrackListRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId
    val trackList = trackListRepo.allTracks
        .onEach {
            Log.d(TAG, "allTracks: updatet")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val unvotedTracks = trackList.map { list ->
        list.filter { it.vote == null }
    }.onEach {
        Log.d(TAG, "unvotedTracks: updatet ${it.map { it.name }}")
    }

    val swipeTracks =
    unvotedTracks.map { list ->
        listOfNotNull(list.getOrNull(0), list.getOrNull(1))
    }


    private val _swipeMode = MutableStateFlow(false)
    val swipeMode = _swipeMode.asStateFlow()


    init {
        loadTrackList()
    }

    private fun loadTrackList() {
        viewModelScope.launch {
            try {
                trackListRepo.loadTracksWithVotes(playlistId)

                // TODO response check und auf 401 reagieren
//                val bla = trackList.value.map { it.id }
//                Log.d(TAG, "loadTrackList: $bla")
//                Log.d(TAG, "loadAllPlaylists: success")
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
            }
        }
    }

    private fun onChangeVote(track: Track, newVote: VoteOption) {
        Log.d(TAG, "onChangeVote: $track")
        trackListRepo.updateVote(playlistId, track.id, newVote)
    }

    fun onSwipe(dir: SwipeDirection, track: Track) {
        onChangeVote(track, VoteOption.fromSwipeDirection(dir))
    }

    fun switchSwipeMode(isOn: Boolean) {
        _swipeMode.value = isOn
        if (isOn) {

        }
    }

}
