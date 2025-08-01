package de.janaja.playlistpurger.shared.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val id: String,
    val name: String,
    @SerialName("duration_ms")
    val durationMillis: Int,
    val album: AlbumDto,
    val artists: List<ArtistDto>,
    // album
    // href
    // is local
    // added by
    // added at??
)