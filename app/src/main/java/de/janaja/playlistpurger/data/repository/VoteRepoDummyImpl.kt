package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Vote
import de.janaja.playlistpurger.data.model.VoteOption

class VoteRepoDummyImpl: VoteRepo {

    private val dummyData = mutableListOf(
        Vote(
            playlistId = "TODO()",
            trackId = "TODO()",
            userId = "TODO()",
            voteOption = VoteOption.KEEP
        )
    )

    override fun getVotesForPlaylist(playlistId: String, userId: String): List<Vote> {
        TODO("Not yet implemented")
    }

}