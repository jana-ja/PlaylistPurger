package de.janaja.playlistpurger.data.remote.vote

import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption

// TODO change to api interface?
interface VoteApi {

    fun getVotesForPlaylist(playlistId: String, userId: String): Result<List<Vote>>
    fun getAllVotesForPlaylist(playlistId: String): Result<List<Vote>>
    fun upsertVote(playlistId: String, trackId: String, userId: String, newVote: VoteOption)
}