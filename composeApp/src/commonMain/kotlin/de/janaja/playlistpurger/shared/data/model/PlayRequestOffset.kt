package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayRequestOffset(
    val uri: String
)
