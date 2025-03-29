package de.janaja.playlistpurger.data.remote

import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.model.PlaylistOwner
import de.janaja.playlistpurger.data.model.PlaylistTracks

object PreviewData {
    val playlist = Playlist(
        id = "ijgüo",
        name = "Playlist Test",
        description = "Description of Playlist",
        collaborative = true,
        public = false,
        images = listOf(),
        tracks = PlaylistTracks("", 0),
        owner = PlaylistOwner(
            id = "id",
            displayName = "Name"
        ),
        type = "type",
    )
}