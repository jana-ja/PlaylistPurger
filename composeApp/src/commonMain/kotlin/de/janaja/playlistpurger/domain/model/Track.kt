package de.janaja.playlistpurger.domain.model


data class Track(
    val id: String,
    val name: String,
    val durationMillis: Int,
    val album: Album,
    val artists: List<Artist>,
    val vote: VoteOption? = null
)
