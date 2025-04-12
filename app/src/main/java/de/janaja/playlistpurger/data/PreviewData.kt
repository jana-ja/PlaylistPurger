package de.janaja.playlistpurger.data

import de.janaja.playlistpurger.data.remote.spotify.model.AlbumDto
import de.janaja.playlistpurger.data.remote.spotify.model.ArtistDto
import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistDto
import de.janaja.playlistpurger.data.remote.spotify.model.UserDto
import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistTracksDto
import de.janaja.playlistpurger.data.remote.spotify.model.TrackDto

object PreviewData {
    val previewPlaylist = PlaylistDto(
        id = "ijgüo",
        name = "Playlist Test",
        description = "Description of Playlist",
        collaborative = true,
        public = false,
        images = listOf(),
        tracks = PlaylistTracksDto("", 0),
        owner = UserDto(
            id = "id",
            displayName = "Name"
        ),
        type = "type",
    )
    val previewTrack = TrackDto(
        id = "heufh",
        name = "Track Name",
        durationMillis = 2000000,
        album = AlbumDto(listOf(), ""),
        artists = listOf(ArtistDto("", "Artist Name"))
    )
}