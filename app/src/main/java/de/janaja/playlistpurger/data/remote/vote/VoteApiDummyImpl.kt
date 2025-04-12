package de.janaja.playlistpurger.data.remote.vote

import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption

class VoteApiDummyImpl: VoteApi {
    private val testPlaylistId = "26YTAFslmPya0jBIFSglKd"
    private val myUserId = "janajansen-de"

    private val user2Id = "user2"
    private val user3Id = "user3"

    private val dummyData = mutableListOf(
        Vote(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = myUserId,
            voteOption = VoteOption.KEEP
        ),
        Vote(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = user2Id,
            voteOption = VoteOption.KEEP
        ),
        Vote(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = user3Id,
            voteOption = VoteOption.DONT_CARE
        ),
        Vote(
            playlistId = testPlaylistId,
            trackId = "0y8UKPyJOluqIuacosTKEv",
            userId = myUserId,
            voteOption = VoteOption.REMOVE
        ),
        Vote(
            playlistId = testPlaylistId,
            trackId = "0y8UKPyJOluqIuacosTKEv",
            userId = user2Id,
            voteOption = VoteOption.KEEP
        )
    )

    // TODO: current user id vllt in datastore speichern oder in inem repo halten wo das sinn macht. erstmal den code zu token flow machen, vllt dann code gegen accessToken und refreshToken eintauschen und dabei auch die userId abfragen, dann alles speichern?
    override fun getVotesForPlaylist(playlistId: String, userId: String): List<Vote> {
        return dummyData.filter { it.playlistId == playlistId && it.userId == userId }
    }

    // 572g
    override fun getAllVotesForPlaylist(playlistId: String): List<Vote> {
        return dummyData.filter { it.playlistId == playlistId }
    }

    override fun upsertVote(
        playlistId: String,
        trackId: String,
        userId: String,
        newVote: VoteOption
    ) {

        val oldVote =
            dummyData.firstOrNull { it.playlistId == playlistId && it.trackId == trackId && it.userId == userId }

        dummyData.remove(oldVote)

        dummyData.add(Vote(playlistId, trackId, userId, newVote))
    }

}