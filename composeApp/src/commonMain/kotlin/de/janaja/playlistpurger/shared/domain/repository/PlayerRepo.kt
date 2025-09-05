package de.janaja.playlistpurger.shared.domain.repository

interface PlayerRepo {
    suspend fun play(playlistId: String, trackId: String): Result<Unit>
    suspend fun pause(): Result<Unit>
}