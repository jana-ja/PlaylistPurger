package de.janaja.playlistpurger.data.remote.spotify.model

data class PlaylistResponseDto(
    val items: List<PlaylistDto>,
    val total: Int,
    val limit: Int
)
