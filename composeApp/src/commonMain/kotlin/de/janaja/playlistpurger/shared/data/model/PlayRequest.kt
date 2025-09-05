package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayRequest(
    @SerialName("context_uri")
    val contextUri: String,
    val offset: PlayRequestOffset
)
