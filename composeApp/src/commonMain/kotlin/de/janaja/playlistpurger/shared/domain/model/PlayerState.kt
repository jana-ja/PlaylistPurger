package de.janaja.playlistpurger.shared.domain.model

data class PlayerState(
    val device: Device,
    val progressMs: Long,
    val isPlaying: Boolean,
    val trackId: String,
)
