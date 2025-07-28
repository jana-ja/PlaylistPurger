package de.janaja.playlistpurger.features.settings.domain.usecase

import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo

class UpdateShowSwipeFirstSettingUseCase(
    private val settingsRepo: SettingsRepo
) {
    // should return something? yes
    suspend operator fun invoke(newValue: Boolean) {
        settingsRepo.updateShowSwipeFirst(newValue)
    }
}