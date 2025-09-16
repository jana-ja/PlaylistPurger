package de.janaja.playlistpurger.features.player.data.model

import de.janaja.playlistpurger.features.player.data.model.PlayRequestOffset
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayRequest(
    @SerialName("context_uri")
    val contextUri: String,
    val offset: PlayRequestOffset
)