package de.janaja.playlistpurger.data.model

data class PlaylistResponse(
    val items: List<Playlist>,
    val total: Int,
    val limit: Int
)
