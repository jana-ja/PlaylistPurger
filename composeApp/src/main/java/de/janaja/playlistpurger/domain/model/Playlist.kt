package de.janaja.playlistpurger.domain.model

data class Playlist(
    val id: String,
    val name: String,
    val description: String,
    val collaborative: Boolean,
    val public: Boolean,
    val imageUrl: String?, // nullable?
    val trackCount: Int,
//    val tracks: List<Track>,
    val owner: User,
)
