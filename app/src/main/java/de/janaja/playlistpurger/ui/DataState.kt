package de.janaja.playlistpurger.ui

sealed class DataState<out T> {
    data object Loading: DataState<Nothing>()
    data class Ready<T>(val data: T): DataState<T>()
    data class Error(val message: UiText): DataState<Nothing>()
}