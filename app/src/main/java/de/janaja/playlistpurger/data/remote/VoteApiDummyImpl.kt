package de.janaja.playlistpurger.data.remote

import de.janaja.playlistpurger.data.model.Vote
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.data.repository.DataStoreRepo

class VoteApiDummyImpl(
    private val dataStoreRepo: DataStoreRepo
) : VoteApi {
    private val testPlaylistId = "26YTAFslmPya0jBIFSglKd"
    private val myUserId = "janajansen-de"

    private val dummyData = mutableListOf(
        Vote(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = myUserId,
            voteOption = VoteOption.KEEP
        ),
        Vote(
            playlistId = testPlaylistId,
            trackId = "0y8UKPyJOluqIuacosTKEv",
            userId = myUserId,
            voteOption = VoteOption.REMOVE
        ),
        Vote(
            playlistId = "orshgo",
            trackId = "0y8UKPyJOluqIuacosTKEv",
            userId = "soighjüo",
            voteOption = VoteOption.DONT_CARE
        )
    )
// TODO: current user id vllt in datastore speichern oder in inem repo halten wo das sinn macht. erstmal den code zu token flow machen, vllt dann code gegen accessToken und refreshToken eintauschen und dabei auch die userId abfragen, dann alles speichern?
    override fun getVotesForPlaylist(playlistId: String, userId: String): List<Vote> {
        return dummyData.filter { it.playlistId == playlistId && it.userId == userId }
    }
// 572g
    override fun upsertVote(playlistId: String, trackId: String, userId: String, newVote: VoteOption) {

        val oldVote = dummyData.firstOrNull { it.playlistId == playlistId && it.trackId == trackId && it.userId == userId }

        dummyData.remove(oldVote)

        dummyData.add(Vote(playlistId, trackId, userId, newVote))
    }

}