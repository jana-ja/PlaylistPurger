package de.janaja.playlistpurger.features.track_voting.domain.usecase

import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.player.domain.repo.PlayerRepo
import kotlin.math.max

class AdjustPlaybackPositionUseCase(
    private val playerRepo: PlayerRepo
) {
    // TODO play only track or with playlist?
    suspend operator fun invoke(seekAmountSec: Int): Result<Unit> {
        // get current position
        val playerResult = playerRepo.getPlayerState()
        return playerResult.map { playerState ->
            val currentPosition = playerState.progressMs

            // calculate new position
            val newPosition = max(currentPosition + seekAmountSec * 1000, 0)
            // TODO if position > track length { pause track }

            Log.d("AdjustPlaybackPositionUseCase", "adjust player position by $seekAmountSec from ${currentPosition / 1000}s to ${newPosition / 1000}s")

            // set new position
            playerRepo.seekPosition(newPosition)

        }
    }
}