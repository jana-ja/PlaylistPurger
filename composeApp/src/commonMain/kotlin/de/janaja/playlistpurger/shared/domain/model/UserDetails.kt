package de.janaja.playlistpurger.shared.domain.model

sealed class UserDetails : UserIdentifiable {

    data class Minimal(override val id: String) : UserDetails()
    data class Partial(override val id: String, override val name: String) : UserDetails(), UserWithName
    data class Full(
        override val id: String,
        override val name: String,
        val thumbnailUrl: String
    ) : UserDetails(), UserWithName
}