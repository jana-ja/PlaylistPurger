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
            name = "Name"
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