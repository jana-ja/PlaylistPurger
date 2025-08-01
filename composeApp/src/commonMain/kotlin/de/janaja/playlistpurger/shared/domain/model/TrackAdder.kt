package de.janaja.playlistpurger.shared.domain.model

sealed class TrackAdder {
    data class FullUser(val user: User) : TrackAdder()
    data class IdOnly(val userId: String) : TrackAdder()
}