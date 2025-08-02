package de.janaja.playlistpurger.shared.domain.model

data class Vote(
    val playlistId: String,
    val trackId: String,
    val user: UserDetails,
    val voteOption: VoteOption
)
