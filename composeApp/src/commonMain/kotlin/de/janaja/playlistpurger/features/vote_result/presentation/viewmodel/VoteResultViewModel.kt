package de.janaja.playlistpurger.features.vote_result.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.navigation.VoteResultRoute
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.playlist_overview.presentation.viewmodel.Filter
import de.janaja.playlistpurger.features.vote_result.domain.model.TrackWithVoteResult
import de.janaja.playlistpurger.features.vote_result.domain.usecase.GetVoteResultsUseCase
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

sealed interface TrackWithVoteResultFilter {
    /**
     * Applies this filter to a single track.
     * @return true if the track matches the filter, false otherwise.
     */
    fun matches(track: TrackWithVoteResult): Boolean
}

data class UserFilter(val userId: String) : TrackWithVoteResultFilter {
    override fun matches(track: TrackWithVoteResult): Boolean {
        return track.track.addedBy.id == userId
    }
}

data class VoteOptionFilter(val voteOption: VoteOption?): TrackWithVoteResultFilter {
    override fun matches(track: TrackWithVoteResult): Boolean {
       return track.voteResult.resultOption == voteOption
    }
}

data class TrackNameFilter(val query: String) :
    TrackWithVoteResultFilter {
    override fun matches(track: TrackWithVoteResult): Boolean {
        return track.track.name.lowercase().contains(query.lowercase())
    }
}

data object NoFilter : TrackWithVoteResultFilter {
    override fun matches(track: TrackWithVoteResult): Boolean {
        return true
    }
}

class VoteResultViewModel(
    savedStateHandle: SavedStateHandle,
    getVoteResultsUseCase: GetVoteResultsUseCase
) : ViewModel() {
    private val args = savedStateHandle.toRoute<VoteResultRoute>()
    private val playlistId = args.playlistId
    val playlistName = args.playlistName

    private val _activeFilters = MutableStateFlow<List<TrackWithVoteResultFilter>>(listOf(NoFilter))
    val activeFilters: StateFlow<List<TrackWithVoteResultFilter>> = _activeFilters.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val dataState = getVoteResultsUseCase(playlistId)
        .combine(_activeFilters) { result, filters ->
            result.fold(
                onSuccess = { result ->
                    val filteredResult = result.tracksWithVoteResult.filter { track ->
                        filters.all { filter ->
                            if (filter is NoFilter) true else filter.matches(track)
                        }
                    }
                    DataState.Ready(result.copy(tracksWithVoteResult = filteredResult))
                },
                onFailure = { e ->
                    if (e is DataException) {
                        DataState.Error(UiText.DynamicString("${e.message}: ${e.cause}"))//e.toStringResId())
                    } else {
                        DataState.Error(UiText.DynamicString("${e.message}: ${e.cause}"))//UiText.StringResourceId(Res.string.generic_error_message))
                    }
                }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataState.Loading
        )

    fun onClickVoteOptionHeader(voteOption: VoteOption?) {
        val filter = VoteOptionFilter(voteOption)
        if (_activeFilters.value.contains(filter)) {
            removeFilter(filter)
        } else {
            _activeFilters.value = (_activeFilters.value.filterNot { it is VoteOptionFilter } + filter)
        }
    }

    fun onClickUserHeader(userId: String) {
        val filter = UserFilter(userId)
        if (_activeFilters.value.contains(filter)) {
            removeFilter(filter)
        } else {
            _activeFilters.value = (_activeFilters.value.filterNot { it is UserFilter } + filter)
        }
    }

    fun onSearchTextChange(value: String) {
        _searchQuery.value = value

        if (_searchQuery.value.isNotBlank()) {
            _activeFilters.value = (_activeFilters.value.filterNot { it is TrackNameFilter } + TrackNameFilter(value))
        } else {
            _activeFilters.value = _activeFilters.value.filterNot { it is TrackNameFilter }
        }
    }

    fun addFilter(filter: TrackWithVoteResultFilter) {
        viewModelScope.launch {
            _activeFilters.value = (_activeFilters.value.filterNot { it is NoFilter } + filter).distinct()
        }
    }

    fun removeFilter(filter: TrackWithVoteResultFilter) {
        viewModelScope.launch {
            _activeFilters.value = _activeFilters.value.filterNot { it == filter }.ifEmpty { listOf(NoFilter) }
        }
    }

    fun removeFilterType(filterType: KClass<out TrackWithVoteResultFilter>) {
        viewModelScope.launch {
            _activeFilters.value = _activeFilters.value.filterNot { filterType.isInstance(it) }.ifEmpty { listOf(NoFilter) }
        }
    }
}