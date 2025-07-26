package de.janaja.playlistpurger.features.settings.domain.repo

import kotlinx.coroutines.flow.Flow

interface SettingsRepo {
    val showSwipeFirstFlow: Flow<Boolean?>

    suspend fun updateShowSwipeFirst(value: Boolean)
}