package de.janaja.playlistpurger.features.playlist_overview.domain.model

import de.janaja.playlistpurger.shared.domain.model.UserWithName

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val collaborative: Boolean,
    val public: Boolean,
    val imageUrl: String?, // nullable?
    val trackCount: Int,
//    val tracks: List<Track>,
    val owner: UserWithName
)
