package de.janaja.playlistpurger.data.remote.spotify.model

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
