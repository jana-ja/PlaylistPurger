package de.janaja.playlistpurger.features.playlist_overview.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.core.ui.util.toStringResId
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.playlist_overview.domain.usecase.GetPlaylistsUseCase
import de.janaja.playlistpurger.shared.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.generic_error_message

sealed class Filter<T>(val function: (T) -> Boolean) {
    data object PlaylistFilterNone :
        Filter<Playlist>({ true }) // will very likely be optimised by JVM, resulting in only fast copying the list

    data class PlaylistFilter(val query: String) :
        Filter<Playlist>({ it.name.lowercase().contains(query.lowercase()) })

    data class TrackFilter(val query: String) :
        Filter<Track>({ it.name.lowercase().contains(query.lowercase()) })

    data class OwnerFilter(val query: String) :
        Filter<Playlist>({ it.owner?.name?.lowercase()?.contains(query.lowercase()) ?: false })

    data object NoFilter : Filter<Nothing>({ true })
}


class PlaylistOverviewViewModel(
    getPlaylistsUseCase: GetPlaylistsUseCase
) : ViewModel() {

    private val TAG = "PlaylistOverviewViewModel"


    private val _filtering =
        MutableStateFlow<Filter<Playlist>>(Filter.PlaylistFilterNone)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    // TODO hätte gerne eine funktion die filterung und sortierung allegmein anwendet, dann macht meine filter sealed class sinn sonst eher nicht?

    val dataState =
        getPlaylistsUseCase()
            .combine(_filtering) { playlistResult, filter ->
                playlistResult.fold(
                    onSuccess = {
                        DataState.Ready(it.filter(filter.function))
                    },
                    onFailure = { e ->
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


    fun onSearchTextChange(value: String) {
        _searchQuery.value = value

        if (_searchQuery.value.isNotBlank()) {
            _filtering.value = Filter.PlaylistFilter(value)
        } else {
            _filtering.value = Filter.PlaylistFilterNone
        }
    }
}