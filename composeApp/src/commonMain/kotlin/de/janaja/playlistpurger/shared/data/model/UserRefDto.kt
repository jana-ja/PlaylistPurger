package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails
import kotlinx.serialization.Serializable

@Serializable
data class UserRefDto(
    val id: String
)

fun UserRefDto.toUserDetails(): UserDetails.Minimal {
    return UserDetails.Minimal(this.id)
}
