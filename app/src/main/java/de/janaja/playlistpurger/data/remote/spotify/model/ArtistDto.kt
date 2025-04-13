package de.janaja.playlistpurger.data.remote.spotify.model

import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto (
    val id: String,
    val name: String
)
/*
{
            "external_urls": {
              "spotify": "string"
            },
            "href": "string",
            "id": "string",
            "name": "string",
            "type": "artist",
            "uri": "string"
          }
 */