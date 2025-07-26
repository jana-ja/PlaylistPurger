package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.Album
import kotlinx.serialization.Serializable

@Serializable
data class AlbumDto(
    val id: String,
    val images: List<ImageDto>,
    val name: String
)
fun AlbumDto.toAlbum(): Album {
    return Album(
        id = this.id,
        name = this.name,
        imageUrl = this.images.firstOrNull()?.url
    )
}
/*
"album": {
          "album_type": "compilation",
          "total_tracks": 9,
          "available_markets": [
            "CA",
            "BR",
            "IT"
          ],
          "external_urls": {
            "spotify": "string"
          },
          "href": "string",
          "id": "2up3OPMp9Tb4dAKM2erWXQ",
          "images": [
            {
              "url": "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
              "height": 300,
              "width": 300
            }
          ],
          "name": "string",
          "release_date": "1981-12",
          "release_date_precision": "year",
          "restrictions": {
            "reason": "market"
          },
 */
