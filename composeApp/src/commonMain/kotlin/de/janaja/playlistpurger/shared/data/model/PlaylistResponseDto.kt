package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponseDto(
    val items: List<PlaylistDto>,
    val total: Int,
    val limit: Int
)
