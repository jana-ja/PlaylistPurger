package de.janaja.playlistpurger.features.settings.domain.usecase

import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo

class UpdateShowSwipeFirstSettingUseCase(
    private val settingsRepo: SettingsRepo
) {
    suspend operator fun invoke(newValue: Boolean): Result<Unit> {
        return settingsRepo.updateShowSwipeFirst(newValue)
    }
}