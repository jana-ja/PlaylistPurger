package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    @SerialName("display_name")
//    @Json(name = "display_name")
    val displayName: String = "Unknown User", // in playlist tracks: added by hat dieses field nicht
)
fun UserDto.toUser(): User {
    return User(
        id = this.id,
        name = this.displayName
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
