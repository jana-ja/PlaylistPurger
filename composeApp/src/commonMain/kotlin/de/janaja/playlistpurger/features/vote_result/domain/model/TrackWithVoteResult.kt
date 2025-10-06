package de.janaja.playlistpurger.features.vote_result.domain.model

import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.VoteResult

data class TrackWithVoteResult(
    val track: Track,
    val voteResult: VoteResult
)
