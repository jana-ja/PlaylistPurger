package de.janaja.playlistpurger.shared.data.remote.vote

import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption

interface VoteApi {

    fun getUsersVotesForPlaylist(playlistId: String, userId: String): Result<List<Vote>>
    fun getAllVotesForPlaylist(playlistId: String): Result<List<Vote>>
    fun upsertVote(playlistId: String, trackId: String, userId: String, newVote: VoteOption)
}