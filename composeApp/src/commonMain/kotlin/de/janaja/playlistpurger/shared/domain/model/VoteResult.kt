package de.janaja.playlistpurger.shared.domain.model

data class VoteResult(
    val votes: List<Vote>,
    val resultOption: VoteOption?
)
