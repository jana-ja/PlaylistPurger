package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPartialDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String
)

fun UserPartialDto.toUserDetails(): UserDetails.Partial {
    return UserDetails.Partial(this.id, this.displayName)
}
