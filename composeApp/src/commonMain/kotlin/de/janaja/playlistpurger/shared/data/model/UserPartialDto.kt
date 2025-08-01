package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPartialDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String
)
