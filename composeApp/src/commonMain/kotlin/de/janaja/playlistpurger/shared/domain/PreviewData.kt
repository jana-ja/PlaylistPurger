package de.janaja.playlistpurger.shared.domain

import de.janaja.playlistpurger.shared.domain.model.Album
import de.janaja.playlistpurger.shared.domain.model.Artist
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.User

object PreviewData {
    val previewPlaylist = Playlist(
        id = "ijgüo",
        name = "Playlist Test",
        description = "Description of Playlist",
        collaborative = true,
        public = false,
        imageUrl = "",
        trackCount = 1,
        owner = User(
            id = "id",
            name = "Name",
            thumbnailImage = "https://i.scdn.co/image/ab67757000003b82f63072e4fad4e5170c1fda52"
        ),
    )
    val previewTrack = Track(
        id = "heufh",
        name = "Track Name",
        durationMillis = 2000000,
        album = Album("id", "", ""),
        artists = listOf(Artist("", "Artist Name"))
    )
}