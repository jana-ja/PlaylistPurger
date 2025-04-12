package de.janaja.playlistpurger.data.remote.spotify.model

import com.squareup.moshi.Json

data class TrackWrapperDto(
    val track: TrackDto,
    @Json(name = "added_by")
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
