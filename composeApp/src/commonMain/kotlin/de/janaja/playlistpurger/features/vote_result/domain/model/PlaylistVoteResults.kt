package de.janaja.playlistpurger.features.vote_result.domain.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails
import de.janaja.playlistpurger.shared.domain.model.VoteOption

data class PlaylistVoteResults(
    val playlistId: String,
    val stats: Map<VoteOption?, Int>,
    val collaborators: List<UserDetails>,
    val tracksWithVoteResult: List<TrackWithVoteResult>
)