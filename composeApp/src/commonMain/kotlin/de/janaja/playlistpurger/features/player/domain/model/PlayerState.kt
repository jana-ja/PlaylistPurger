package de.janaja.playlistpurger.features.player.domain.model

data class PlayerState(
    val device: Device,
    val progressMs: Long,
    val isPlaying: Boolean,
    val trackId: String,
)