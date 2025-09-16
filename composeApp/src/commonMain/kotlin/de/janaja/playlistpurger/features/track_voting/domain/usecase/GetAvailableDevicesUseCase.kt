package de.janaja.playlistpurger.features.track_voting.domain.usecase

import de.janaja.playlistpurger.features.player.domain.model.Device
import de.janaja.playlistpurger.features.player.domain.repo.PlayerRepo

class GetAvailableDevicesUseCase(
    private val playerRepo: PlayerRepo
) {
    suspend operator fun invoke(): Result<List<Device>> {
        return playerRepo.getAvailableDevices()
    }
}