package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.domain.repository.TokenRepo
import de.janaja.playlistpurger.domain.repository.TrackListRepo
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import de.janaja.playlistpurger.ui.TrackListRoute
import de.janaja.playlistpurger.ui.component.SwipeDirection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrackListVoteViewModel(
    private val tokenRepo: TokenRepo,
    private val settingsRepo: SettingsRepo,
    private val trackListRepo: TrackListRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId

    private val _swipeModeOn = MutableStateFlow(true)
    val swipeModeOn = _swipeModeOn.asStateFlow()

    val trackList = trackListRepo.allTracks
        .onEach {
            Log.d(TAG, "allTracks: updated")
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    // swipe
    private val unvotedTracks = trackList.map { list ->
        list.filter { it.vote == null }
    }

    val swipeTracks =
    unvotedTracks.map { list ->
        listOfNotNull(list.getOrNull(0), list.getOrNull(1))
    }

    init {
        loadTrackList()

        viewModelScope.launch {
            settingsRepo.showSwipeFirstFlow.first()?.let {
                _swipeModeOn.value = it
            }
        }
    }

    fun switchSwipeMode(isOn: Boolean) {
        _swipeModeOn.value = isOn
    }

    private fun loadTrackList() {
        viewModelScope.launch {
            try {
                trackListRepo.loadTracksWithOwnVotes(playlistId)

                // TODO response check und auf 401 reagieren
//                val bla = trackList.value.map { it.id }
//                Log.d(TAG, "loadTrackList: $bla")
//                Log.d(TAG, "loadAllPlaylists: success")
            } catch (e: Exception) {
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
            }
        }
    }

    fun onChangeVote(track: Track, newVote: VoteOption) {
        Log.d(TAG, "onChangeVote: $track")
        trackListRepo.updateVote(playlistId, track.id, newVote)
    }

    // swipe mode
    fun onSwipe(dir: SwipeDirection, track: Track) {
        // TODO darf nicht passieren dass das null ist, dann wäre gültiger down swipe passiert, aber swipe card sollte das blocken
        VoteOption.fromSwipeDirection(dir)?.let {
            onChangeVote(track, it)
        }
    }
}
