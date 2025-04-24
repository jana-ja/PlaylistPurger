package de.janaja.playlistpurger.domain

import de.janaja.playlistpurger.domain.model.Album
import de.janaja.playlistpurger.domain.model.Artist
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.model.User

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