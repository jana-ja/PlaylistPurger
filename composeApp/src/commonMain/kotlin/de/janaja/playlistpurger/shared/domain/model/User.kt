package de.janaja.playlistpurger.shared.domain.model

data class User(
    val id: String,
    val name: String, // optional? default value?
    val thumbnailImage: String?
)
