package de.janaja.playlistpurger.data.remote

import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.model.User
import de.janaja.playlistpurger.data.model.PlaylistTracks
import de.janaja.playlistpurger.data.model.Track

object PreviewData {
    val playlist = Playlist(
        id = "ijgüo",
        name = "Playlist Test",
        description = "Description of Playlist",
        collaborative = true,
        public = false,
        images = listOf(),
        tracks = PlaylistTracks("", 0),
        owner = User(
            id = "id",
            displayName = "Name"
        ),
        type = "type",
    )
    val track = Track(
        id = "heufh",
        name = "Track track",
        durationMillis = 2000000
    )
}