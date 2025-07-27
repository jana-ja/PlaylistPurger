package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    @SerialName("display_name")
    val displayName: String = "Unknown User", // in playlist tracks: added by hat dieses field nicht
    val images: List<ImageDto>? = null // does not always exist, for example when loading a playlist the owner is a user without images. when loading a user by itself it has images // 0 -> big image 300:300, 1 -> thumbnail 64:64
    // TODO maybe split to user and detailuser?
)
fun UserDto.toUser(): User {
    return User(
        id = this.id,
        name = this.displayName,
        thumbnailImage = this.images?.getOrNull(1)?.url
    )
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
