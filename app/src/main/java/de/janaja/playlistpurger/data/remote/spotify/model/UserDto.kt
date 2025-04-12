package de.janaja.playlistpurger.data.remote.spotify.model

import com.squareup.moshi.Json

data class UserDto(
    val id: String,
    @Json(name = "display_name")
    val displayName: String = "Unknown User", // in playlist tracks: added by hat dieses field nicht
)
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
