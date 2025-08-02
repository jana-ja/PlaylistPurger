package de.janaja.playlistpurger.core.ui.model

import de.janaja.playlistpurger.core.ui.util.UiText

sealed class DataState<out T> {
    data object Loading: DataState<Nothing>()
    data class Ready<T>(val data: T): DataState<T>()
    data class Error(val message: UiText): DataState<Nothing>()
}