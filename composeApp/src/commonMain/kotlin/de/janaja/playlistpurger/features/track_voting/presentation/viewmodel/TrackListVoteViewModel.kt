package de.janaja.playlistpurger.features.track_voting.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo
import de.janaja.playlistpurger.core.ui.TrackListRoute
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.util.handleDataException
import de.janaja.playlistpurger.core.ui.component.SwipeDirection
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.settings.domain.usecase.ObserveSettingsUseCase
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class TrackListVoteViewModel(
    private val authService: AuthService,
    private val trackListRepo: TrackListRepo,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
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
    private val allTracks = _dataState
        .mapNotNull {
            if (it is DataState.Ready) {
                it.data
            } else {
                null
            }
        }
    private val unvotedTracks = allTracks
        .map { trackList ->
            trackList.filter { it.vote == null }
        }
        .distinctUntilChanged()

    val swipeTracks =
        unvotedTracks.map { list ->
            listOfNotNull(list.getOrNull(0), list.getOrNull(1))
        }

    val allTracksCount = allTracks
        .map { it.count() }

    val votedTracksCount = allTracksCount
        .combine(unvotedTracks) { allCount, unvotedTracks ->
            allCount - unvotedTracks.count()
        }

    // ui states
    private val _swipeModeOn = MutableStateFlow(true)
    val swipeModeOn = _swipeModeOn.asStateFlow()


    init {
        observeTrackList()

        viewModelScope.launch {
            observeSettingsUseCase().first().let {
                _swipeModeOn.value = it.showSwipeFirst
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
                Log.e(TAG, "loadAllPlaylists: ", e)
                handleDataException(
                    e = e,
                    onRefresh = {
                        viewModelScope.launch {
                            if (authService.refreshToken().isSuccess) {
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
                Log.e(TAG, "onChangeVote: ", e)
                handleDataException(
                    e = e,
                    onRefresh = {
                        viewModelScope.launch {
                            if (authService.refreshToken().isSuccess) {
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
