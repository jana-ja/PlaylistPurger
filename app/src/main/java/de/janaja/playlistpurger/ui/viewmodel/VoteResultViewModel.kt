package de.janaja.playlistpurger.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.data.remote.spotify.model.TrackDto
import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.data.repository.TrackListRepo
import de.janaja.playlistpurger.ui.VoteResultRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class VoteResultViewModel (
    savedStateHandle: SavedStateHandle,
    private val trackListRepo: TrackListRepo,
) : ViewModel() {
    private val args = savedStateHandle.toRoute<VoteResultRoute>()
    private val playlistId = args.playlistId


    val blub = MutableStateFlow<List<Pair<TrackDto, List<Vote>>>>(emptyList())

    init {
        getAllVotes()
    }

    private fun getAllVotes() {
        viewModelScope.launch {
            blub.value = trackListRepo.loadTracksWithAllVotes(playlistId)
        }
    }
}