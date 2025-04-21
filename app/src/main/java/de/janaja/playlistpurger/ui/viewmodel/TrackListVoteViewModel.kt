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
import de.janaja.playlistpurger.ui.DataState
import de.janaja.playlistpurger.ui.TrackListRoute
import de.janaja.playlistpurger.ui.component.SwipeDirection
import de.janaja.playlistpurger.ui.handleDataException
import kotlinx.coroutines.Job
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

    private var observeTrackListJob: Job? = null

    // data states
    private val _dataState =
        MutableStateFlow<DataState<List<Track>>>(DataState.Loading)
    val dataState = _dataState.asStateFlow()

    // swipe
    private val unvotedTracks = _dataState.map {
        if (it is DataState.Ready) {
            it.data.filter { it.vote == null }
        } else {
            emptyList()
        }
    }

    val swipeTracks =
        unvotedTracks.map { list ->
            listOfNotNull(list.getOrNull(0), list.getOrNull(1))
        }

    // ui states
    private val _swipeModeOn = MutableStateFlow(true)
    val swipeModeOn = _swipeModeOn.asStateFlow()


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

    private fun observeTrackList() {
        observeTrackListJob?.cancel()
        observeTrackListJob = viewModelScope.launch {
            val result = trackListRepo.loadTracksWithOwnVotes(playlistId)

            result.onSuccess { trackListFlow ->
                trackListFlow.collect {
                    _dataState.value = DataState.Ready(it)
                }
            }.onFailure { e ->
                Log.e(TAG, "loadAllPlaylists: ${e.localizedMessage}")
                handleDataException(
                    e = e,
                    onRefresh = {
                        viewModelScope.launch {
                            if (authService.refreshToken()) {
                                observeTrackList()
                            }
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

    fun onChangeVote(track: Track, newVote: VoteOption) {
        viewModelScope.launch {
            Log.d(TAG, "onChangeVote: $track")
            val result = trackListRepo.updateVote(playlistId, track.id, newVote)
            result.onFailure { e ->
                Log.e(TAG, "onChangeVote: ", e.cause)
                handleDataException(
                    e = e,
                    onRefresh = {
                        viewModelScope.launch {
                            if (authService.refreshToken()) {
                                onChangeVote(track, newVote)
                            }
                        }
                    },
                    onLogout = {
                        viewModelScope.launch {
                            authService.logout()
                        }
                    },
                    onUpdateErrorMessage = {
                        // TODO just show toast instead?
                        _dataState.value = DataState.Error(it)
                    }
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
