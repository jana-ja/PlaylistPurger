package de.janaja.playlistpurger.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    val showSwipeFirstFlow: Flow<Boolean?>

    suspend fun updateShowSwipeFirst(value: Boolean)
}