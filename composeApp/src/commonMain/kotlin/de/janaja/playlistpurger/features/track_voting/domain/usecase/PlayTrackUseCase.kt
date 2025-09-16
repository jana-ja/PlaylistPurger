package de.janaja.playlistpurger.features.track_voting.domain.usecase

import de.janaja.playlistpurger.features.player.domain.repo.PlayerRepo
import de.janaja.playlistpurger.shared.domain.model.Track

class PlayTrackUseCase(
    private val playerRepo: PlayerRepo
) {
    // TODO play only track or with playlist?
    suspend operator fun invoke(playlistId: String, track: Track, deviceId: String): Result<Unit> {
        return playerRepo.play(playlistId, track.id, deviceId)
    }
}