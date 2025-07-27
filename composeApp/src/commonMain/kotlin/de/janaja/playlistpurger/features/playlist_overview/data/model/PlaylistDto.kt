package de.janaja.playlistpurger.features.playlist_overview.data.model

import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.shared.data.model.ImageDto
import de.janaja.playlistpurger.shared.data.model.UserDto
import de.janaja.playlistpurger.shared.data.model.UserRefDto
import de.janaja.playlistpurger.shared.data.model.toUser
import de.janaja.playlistpurger.shared.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val id: String,
    val name: String,
    val description: String,
    val collaborative: Boolean,
    val public: Boolean,
    val images: List<ImageDto>,
    val tracks: PlaylistTracksDto,
    val owner: UserRefDto, // displayName but no images
    val type: String,
)
fun PlaylistDto.toPlaylist(owner: User?): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        collaborative = this.collaborative,
        public = this.public,
        imageUrl = this.images.firstOrNull()?.url,
        trackCount = this.tracks.total,
        owner = owner
    )
}

/*
{
      "collaborative": false,
      "description": "string",
      "external_urls": {
        "spotify": "string"
      },
      "href": "string",
      "id": "string",
      "images": [
        {
          "url": "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
          "height": 300,
          "width": 300
        }
      ],
      "name": "string",
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
      "public": false,
      "snapshot_id": "string",
      "tracks": {
        "href": "string",
        "total": 0
      },
      "type": "string",
      "uri": "string"
    }
 */
