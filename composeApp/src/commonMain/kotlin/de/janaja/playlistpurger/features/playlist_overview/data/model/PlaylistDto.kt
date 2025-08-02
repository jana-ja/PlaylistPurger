package de.janaja.playlistpurger.features.playlist_overview.data.model

import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.shared.data.model.ImageDto
import de.janaja.playlistpurger.shared.data.model.UserPartialDto
import de.janaja.playlistpurger.shared.domain.model.UserWithName
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
    val owner: UserPartialDto,
    val type: String,
)
fun PlaylistDto.toPlaylist(owner: UserWithName): Playlist {
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