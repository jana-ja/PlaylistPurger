package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.shared.data.model.VoteDto
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption

interface VoteApi {

    fun getUsersVotesForPlaylist(playlistId: String, userId: String): Result<List<VoteDto>>
    fun getAllVotesForPlaylist(playlistId: String): Result<List<VoteDto>>
    fun upsertVote(playlistId: String, trackId: String, userId: String, newVote: VoteOption): Result<Unit>
}