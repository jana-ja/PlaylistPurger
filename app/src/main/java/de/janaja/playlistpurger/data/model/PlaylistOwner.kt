package de.janaja.playlistpurger.data.model

import com.squareup.moshi.Json

data class PlaylistOwner(
    val id: String,
    @Json(name = "display_name")
    val displayName: String,
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