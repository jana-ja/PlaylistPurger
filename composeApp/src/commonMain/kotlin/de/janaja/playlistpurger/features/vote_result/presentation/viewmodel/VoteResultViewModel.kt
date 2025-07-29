package de.janaja.playlistpurger.features.vote_result.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.ui.VoteResultRoute
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.core.ui.util.toStringResId
import de.janaja.playlistpurger.features.vote_result.domain.usecase.GetTracksWithAllVotesUseCase
import kotlinx.coroutines.flow.map
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.generic_error_message

class VoteResultViewModel(
    savedStateHandle: SavedStateHandle,
    getTracksWithAllVotesUseCase: GetTracksWithAllVotesUseCase
) : ViewModel() {
    private val args = savedStateHandle.toRoute<VoteResultRoute>()
    private val playlistId = args.playlistId

    val dataState = getTracksWithAllVotesUseCase(playlistId)
        .map { result ->
            result.fold(
                onSuccess = {
                    DataState.Ready(it)
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
}