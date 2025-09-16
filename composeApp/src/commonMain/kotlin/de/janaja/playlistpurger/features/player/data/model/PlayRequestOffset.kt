package de.janaja.playlistpurger.features.player.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayRequestOffset(
    val uri: String
)