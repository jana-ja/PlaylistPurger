package de.janaja.playlistpurger.shared.domain.repository

import de.janaja.playlistpurger.shared.domain.model.PlayerState

interface PlayerRepo {
    suspend fun play(playlistId: String, trackId: String): Result<Unit>
    suspend fun pause(): Result<Unit>
    suspend fun seekPosition(newPosMs: Long): Result<Unit>
    suspend fun getPlayerState(): Result<PlayerState>
}