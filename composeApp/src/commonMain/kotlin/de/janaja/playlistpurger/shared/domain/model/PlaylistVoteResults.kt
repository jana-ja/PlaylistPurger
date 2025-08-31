package de.janaja.playlistpurger.shared.domain.model

data class PlaylistVoteResults(
    val playlistId: String,
    val collaborators: List<UserDetails>,
    val tracksWithVotes: List<Pair<Track, VoteResult>>
)
