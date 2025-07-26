package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistTracksDto(
    val href: String,
    val total: Int,
)

/*
"tracks": {
        "href": "string",
        "total": 0
      },
 */
