package de.janaja.playlistpurger.features.vote_result.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.core.ui.VoteResultRoute
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.util.handleDataException
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VoteResultViewModel(
    savedStateHandle: SavedStateHandle,
    private val authService: AuthService,
    private val trackListRepo: TrackListRepo,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<VoteResultRoute>()
    private val playlistId = args.playlistId

    private val _dataState =
        MutableStateFlow<DataState<List<Pair<Track, List<Vote>>>>>(DataState.Loading)
    val dataState = _dataState.asStateFlow()

    init {
        getAllVotes()
    }

    private fun getAllVotes() {
        viewModelScope.launch {
            val result = trackListRepo.loadTracksWithAllVotes(playlistId)
            result.onSuccess {
                _dataState.value = DataState.Ready(it)
            }.onFailure { e ->
                handleDataException(
                    e = e,
                    onRefresh = {
                        viewModelScope.launch {
                            if (authService.refreshToken().isSuccess) {
                                getAllVotes()
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
}