package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFullDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    val images: List<ImageDto> // 0 -> big image 300:300, 1 -> thumbnail 64:64
)

fun UserFullDto.toUser(): UserDetails.Full {
    val thumbnailUrl = this.images.getOrNull(1)?.url
    return UserDetails.Full(
        id = this.id,
        name = this.displayName,
        thumbnailUrl = thumbnailUrl
    )
}