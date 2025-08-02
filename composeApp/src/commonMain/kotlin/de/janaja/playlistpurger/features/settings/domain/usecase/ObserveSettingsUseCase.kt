package de.janaja.playlistpurger.features.settings.domain.usecase

import de.janaja.playlistpurger.features.settings.domain.Settings
import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase(
    private val settingsRepo: SettingsRepo
) {
    operator fun invoke(): Flow<Settings> {
        return settingsRepo.observeSettings()
    }
}