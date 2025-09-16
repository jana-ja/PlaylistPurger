package de.janaja.playlistpurger.features.track_voting.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.ui.component.SwipeDirection
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.navigation.TrackListRoute
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.core.ui.util.toStringResId
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.player.domain.model.Device
import de.janaja.playlistpurger.features.settings.domain.usecase.ObserveSettingsUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.AdjustPlaybackPositionUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.GetAvailableDevicesUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.ObserveTracksWithOwnVotesUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.PauseUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.PlayTrackUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.UpsertVoteUseCase
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.generic_error_message

class TrackListVoteViewModel(
    observeTracksWithOwnVotesUseCase: ObserveTracksWithOwnVotesUseCase,
    private val upsertVoteUseCase: UpsertVoteUseCase,
    private val observeSettingsUseCase: ObserveSettingsUseCase,
    private val getAvailableDevicesUseCase: GetAvailableDevicesUseCase,
    private val playTrackUseCase: PlayTrackUseCase,
    private val pauseUseCase: PauseUseCase,
    private val adjustPlaybackPositionUseCase: AdjustPlaybackPositionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val TAG = "TrackListViewModel"

    private val args = savedStateHandle.toRoute<TrackListRoute>()
    private val playlistId = args.playlistId
    val playlistName = args.playlistName

    // data states
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
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                emptyList()
            )

    val allTracksCount = allTracks
        .map { it.count() }

    val votedTracksCount = allTracksCount
        .combine(unvotedTracks) { allCount, unvotedTracks ->
            allCount - unvotedTracks.count()
        }

    private val _availableDevices = MutableStateFlow(listOf<Device>())
    val availableDevices = _availableDevices.asStateFlow()

    private var _selectedDevice = MutableStateFlow<Device?>(null)
    val selectedDevice = _selectedDevice.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    // ui states
    private val _swipeModeOn = MutableStateFlow(true)
    val swipeModeOn = _swipeModeOn.asStateFlow()


    init {
        viewModelScope.launch {
            observeSettingsUseCase().first().let {
                _swipeModeOn.value = it.showSwipeFirst
            }
            getAvailableDevices()
        }
    }

    fun switchSwipeMode(isOn: Boolean) {
        _swipeModeOn.value = isOn
    }

    fun onChangeVote(track: Track, newVote: VoteOption) {
        viewModelScope.launch {
            Log.d(TAG, "onChangeVote: $track")

            val result = upsertVoteUseCase(playlistId, track.id, newVote)
            // TODO check if is playing? check if successful?
            if (_isPlaying.value) {
                pauseSwipeTrack()
            }
            result.onFailure {
                // TODO error handling
                // upsert failed, old value was already restored
                // show something to user
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

    fun playPauseSwipeTrack() {
        if (_isPlaying.value)
            pauseSwipeTrack()
        else
            playSwipeTrack()
    }

    private fun getAvailableDevices() {
        viewModelScope.launch {
            val result = getAvailableDevicesUseCase()
            result.fold(
                onSuccess = { devices ->
                    _availableDevices.value = devices
                    // if one of the devices is selected -> write in stateflow
                    _selectedDevice.value = devices.find { it.isActive }
                },
                onFailure = {
                    Log.e(TAG, "getAvailableDevices: ", it)
                    // TODO error message with snackbar
                }
            )
        }
    }

    private fun playSwipeTrack() {
        // TODO currently doesnt resume the song if i click play a second time
        val state = dataState.value
        val track = swipeTracks.value.firstOrNull()

        if (state is DataState.Ready<List<Track>> && track != null) {
            viewModelScope.launch {
                Log.d("TrackListVoteViewModel", "$playlistId, ${track.name} mit ${track.id}")
                val playResult = playTrackUseCase(playlistId, track)
                playResult.fold(
                    onSuccess = {
                        _isPlaying.value = true
                    },
                    onFailure = {
                        // TODO error message with snackbar
                        // TODO add specific player state exception types
                    }
                )
            }
        } else {
            // TODO show errors with snackbar
        }
    }

    private fun pauseSwipeTrack() {
        viewModelScope.launch {
            val pauseResult = pauseUseCase()
            pauseResult.fold(
                onSuccess = {
                    _isPlaying.value = false
                },
                onFailure = {
                    // TODO error message with snackbar
                }
            )
        }
    }

    fun forwardTrack() {
        viewModelScope.launch {
            val result = adjustPlaybackPositionUseCase(10)
            result.onFailure {
                // TODO error message with snackbar
            }
        }
    }

    fun rewindTrack() {
        viewModelScope.launch {
            val result = adjustPlaybackPositionUseCase(-10)
            result.onFailure {
                // TODO error message with snackbar
            }
        }
    }

    fun onDeviceChange(device: Device?) {
        _selectedDevice.value = device
        // TODO check if currently is playing and change device
    }
}
