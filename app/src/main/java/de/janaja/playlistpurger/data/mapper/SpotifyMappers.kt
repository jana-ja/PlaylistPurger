package de.janaja.playlistpurger.data.mapper

import de.janaja.playlistpurger.data.remote.spotify.model.AlbumDto
import de.janaja.playlistpurger.data.remote.spotify.model.ArtistDto
import de.janaja.playlistpurger.data.remote.spotify.model.PlaylistDto
import de.janaja.playlistpurger.data.remote.spotify.model.TrackDto
import de.janaja.playlistpurger.data.remote.spotify.model.UserDto
import de.janaja.playlistpurger.domain.model.Album
import de.janaja.playlistpurger.domain.model.Artist
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.model.User
import de.janaja.playlistpurger.domain.model.VoteOption

fun PlaylistDto.toPlaylist(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        collaborative = this.collaborative,
        public = this.public,
        imageUrl = this.images.firstOrNull()?.url,
        trackCount = this.tracks.total,
        owner = this.owner.toUser()
    )
}

fun UserDto.toUser(): User {
    return User(
        id = this.id,
        name = this.displayName
    )
}

fun TrackDto.toTrack(voteOption: VoteOption?): Track {
    return Track(
        id = this.id,
        name = this.name,
        durationMillis = this.durationMillis,
        album = this.album.toAlbum(),
        artists = this.artists.map { it.toArtist() },
        vote = voteOption
    )
}

fun AlbumDto.toAlbum(): Album {
    return Album(
        id = this.id,
        name = this.name,
        imageUrl = this.images.firstOrNull()?.url
    )
}

fun ArtistDto.toArtist(): Artist {
    return Artist(
        id = this.id,
        name = this.name
    )
}