package de.janaja.playlistpurger.features.track_voting.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.core.ui.TrackListRoute
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.util.handleDataException
import de.janaja.playlistpurger.core.ui.component.SwipeDirection
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.core.ui.util.toStringResId
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.settings.domain.usecase.ObserveSettingsUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.ObserveTracksWithOwnVotesUseCase
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.generic_error_message

class TrackListVoteViewModel(
    private val authService: AuthService,
    private val trackListRepo: TrackListRepo,
    private val observeTracksWithOwnVotesUseCase: ObserveTracksWithOwnVotesUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId

    // data states
//    private val _dataState =
//        MutableStateFlow<DataState<List<Track>>>(DataState.Loading)
    val dataState = observeTracksWithOwnVotesUseCase(playlistId)
        .map { result ->
            result.fold(
                onSuccess = {
                    DataState.Ready(it)
                },
                onFailure = { e ->
                    // TODO hier könnte fehler sein wenn initial refresh nicht ging oder wenn im flow iwas passiert (unwahrscheinlich)
                    if (e is DataException) {
                        DataState.Error(e.toStringResId())
                    } else {
                        DataState.Error(UiText.StringResourceId(Res.string.generic_error_message))
                    }
                }
            )
        }
//        .onStart { DataState.Loading }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataState.Loading
        )

    // swipe
    private val allTracks = dataState
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
        viewModelScope.launch {
            observeSettingsUseCase().first().let {
                _swipeModeOn.value = it.showSwipeFirst
            }
        }
    }

    fun switchSwipeMode(isOn: Boolean) {
        _swipeModeOn.value = isOn
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
//                        _dataState.value = DataState.Error(it) // TODO update code to usecase
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
