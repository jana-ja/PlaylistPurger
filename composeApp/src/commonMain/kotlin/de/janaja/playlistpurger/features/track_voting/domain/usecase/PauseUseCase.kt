package de.janaja.playlistpurger.features.track_voting.domain.usecase

import de.janaja.playlistpurger.shared.domain.repository.PlayerRepo

class PauseUseCase(
    private val playerRepo: PlayerRepo
) {
    suspend operator fun invoke(): Result<Unit> {
        return playerRepo.pause()
    }
}