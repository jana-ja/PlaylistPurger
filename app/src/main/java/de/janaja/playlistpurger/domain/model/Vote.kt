package de.janaja.playlistpurger.domain.model

data class Vote(
    val playlistId: String,
    val trackId: String,
    val userId: String,
    val voteOption: VoteOption
)
