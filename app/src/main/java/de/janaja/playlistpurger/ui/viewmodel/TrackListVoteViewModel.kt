package de.janaja.playlistpurger.ui.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import de.janaja.playlistpurger.domain.repository.TrackListRepo
import de.janaja.playlistpurger.ui.TrackListRoute
import de.janaja.playlistpurger.ui.UiText
import de.janaja.playlistpurger.ui.component.SwipeDirection
import de.janaja.playlistpurger.ui.handleDataException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TrackListVoteViewModel(
    private val authService: AuthService,
    private val settingsRepo: SettingsRepo,
    private val trackListRepo: TrackListRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId

    private val _swipeModeOn = MutableStateFlow(true)
    val swipeModeOn = _swipeModeOn.asStateFlow()

    var trackList = MutableStateFlow<List<Track>>(emptyList())

    private val _errorMessage = MutableStateFlow<UiText?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // swipe
    private val unvotedTracks = trackList.map { list ->
        list.filter { it.vote == null }
    }

    val swipeTracks =
    unvotedTracks.map { list ->
        listOfNotNull(list.getOrNull(0), list.getOrNull(1))
    }

    init {
        observeTrackList()

        viewModelScope.launch {
            settingsRepo.showSwipeFirstFlow.first()?.let {
                _swipeModeOn.value = it
            }
        }
    }

    fun switchSwipeMode(isOn: Boolean) {
        _swipeModeOn.value = isOn
    }

    // TODO job for observing?
    private fun observeTrackList() {
        viewModelScope.launch {
                val result = trackListRepo.loadTracksWithOwnVotes(playlistId)

            result.onSuccess { trackListFlow ->
                trackListFlow.collect {
                    // TODO besser machen
                    trackList.value = it
                }
            }.onFailure { e ->
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
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
                    onUpdateErrorMessage = { _errorMessage.value = it }
                )
            }
        }
    }

    fun onChangeVote(track: Track, newVote: VoteOption) {
        viewModelScope.launch {
            Log.d(TAG, "onChangeVote: $track")
            val response = trackListRepo.updateVote(playlistId, track.id, newVote)
            response.onFailure { e ->
                Log.e(TAG, "onChangeVote: ", e.cause)
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
                    onUpdateErrorMessage = { _errorMessage.value = it }
                )
            }
        }
    }

    // swipe mode
    fun onSwipe(dir: SwipeDirection, track: Track) {
        // TODO darf nicht passieren dass das null ist, dann wäre gültiger down swipe passiert, aber swipe card sollte das blocken
        VoteOption.fromSwipeDirection(dir)?.let {
            onChangeVote(track, it)
        }
    }
}
