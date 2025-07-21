package de.janaja.playlistpurger.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import de.janaja.playlistpurger.ui.DataState
import de.janaja.playlistpurger.ui.resultToDataState
import de.janaja.playlistpurger.util.Log
import de.janaja.playlistpurger.util.now
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

sealed class Filter<T>(val function: (T) -> Boolean) {
    data object PlaylistFilterNone : Filter<Playlist>({ true }) // will very likely be optimised by JVM, resulting in only fast copying the list
    data class PlaylistFilter(val query: String) :
        Filter<Playlist>({ it.name.lowercase().contains(query.lowercase()) })

    data class TrackFilter(val query: String) :
        Filter<Track>({ it.name.lowercase().contains(query.lowercase()) })

    data class OwnerFilter(val query: String) :
        Filter<Playlist>({ it.owner.name.lowercase().contains(query.lowercase()) })

    data object NoFilter : Filter<Nothing>({ true })
}


class PlaylistOverviewViewModel(
    private val authService: AuthService,
    private val playListRepo: PlaylistRepo
) : ViewModel() {

    private val TAG = "PlaylistOverviewViewModel"


    private val _filtering =
        MutableStateFlow<Filter<Playlist>>(Filter.PlaylistFilterNone)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // This flow will trigger a refresh/retry
    private val _retryTrigger = MutableStateFlow(LocalDateTime.now()) // Initial trigger

    // This function will be called to initiate a retry
    fun retryLoadPlaylists() {
        _retryTrigger.value = LocalDateTime.now() // Emit new value to trigger
    }


    // TODO hätte gerne eine funktion die filterung und sortierung allegmein anwendet, dann macht meine filter sealed class sinn sonst eher nicht?

    @OptIn(ExperimentalCoroutinesApi::class)
    val dataState =
        _retryTrigger.flatMapLatest { _ ->

            playListRepo.getPlaylists()
                .combine(_filtering) { playlistResult, filter ->


                    // lololo vllt an result extenden statt vm?
                    resultToDataState(
                        apiResult = playlistResult,
                        successTransform = { lel ->

                            return@resultToDataState lel.filter(filter.function)
                        },
                        // TODO doch move refresh and logout inside the function?
                        onRefresh = {
                            viewModelScope.launch {
                                Log.i(TAG, "flow: try refresh token")
                                if (authService.refreshToken()) {
                                    Log.i(TAG, "flow: token refresh successfull")
                                    // TODO test this
                                    _retryTrigger.value = LocalDateTime.now()
                                }
                            }
                        },
                        onLogout = {
                            viewModelScope.launch {
                                authService.logout()
                            }
                        },
                    )
                }
//                    .onStart { emit(DataState.Loading) }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataState.Loading
        )


    fun onSearchTextChange(value: String) {
        _searchQuery.value = value

        // TODO
        if (_searchQuery.value.isNotBlank()) {
            _filtering.value = Filter.PlaylistFilter(value)
        } else {
            _filtering.value = Filter.PlaylistFilterNone
        }
    }
}