package de.janaja.playlistpurger.features.settings.domain.repo

import de.janaja.playlistpurger.features.settings.domain.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepo {

    fun observeSettings(): Flow<Settings>
    suspend fun updateShowSwipeFirst(value: Boolean): Result<Unit>
}