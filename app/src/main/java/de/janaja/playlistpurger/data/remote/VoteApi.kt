package de.janaja.playlistpurger.data.remote

import de.janaja.playlistpurger.data.model.Vote
import de.janaja.playlistpurger.data.model.VoteOption

// TODO change to api interface?
interface VoteApi {

    fun getVotesForPlaylist(playlistId: String, userId: String): List<Vote>
    fun upsertVote(playlistId: String, trackId: String, userId: String, newVote: VoteOption)
}