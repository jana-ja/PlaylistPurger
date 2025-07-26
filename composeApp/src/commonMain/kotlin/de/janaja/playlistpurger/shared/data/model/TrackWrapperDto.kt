package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackWrapperDto(
    val track: TrackDto,
    @SerialName("added_by")
    val addedBy: UserDto, // hat hier keinen display name
)
/*
{
      "added_at": "string",
      "added_by": {
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
        "uri": "string"
      },
      "is_local": false,
      "track": {}
    }
 */
