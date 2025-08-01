package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails
import de.janaja.playlistpurger.shared.domain.model.UserWithName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFullDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String, // in playlist tracks: added by hat dieses field nicht
    val images: List<ImageDto> // 0 -> big image 300:300, 1 -> thumbnail 64:64
)
fun UserFullDto.toUser(): UserWithName {
    val thumbnailUrl = this.images.getOrNull(1)?.url
    return if (thumbnailUrl != null) {
        UserDetails.Full(
            id = this.id,
            name = this.displayName,
            thumbnailUrl = thumbnailUrl
        )
    } else {
        UserDetails.Partial(
            id = this.id,
            name = this.displayName,
        )
    }
}
/*
"owner": {
        "external_urls": {
          "spotify": "string"
        },
        "followers": {
          "href": "string",
          "total": 0
        },
        "href": "string",
        "id": "string",
        "type": "user",
        "uri": "string",
        "display_name": "string"
      },
 */
