package de.janaja.playlistpurger.features.vote_result.domain.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails

data class PlaylistVoteResults(
    val playlistId: String,
    val collaborators: List<UserDetails>,
    val tracksWithVoteResult: List<TrackWithVoteResult>
)