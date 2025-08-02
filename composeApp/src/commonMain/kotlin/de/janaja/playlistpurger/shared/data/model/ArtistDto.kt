package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.Artist
import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto (
    val id: String,
    val name: String
)
fun ArtistDto.toArtist(): Artist {
    return Artist(
        id = this.id,
        name = this.name
    )
}
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