package de.janaja.playlistpurger.shared.data.remote

import de.janaja.playlistpurger.shared.data.model.VoteDto
import de.janaja.playlistpurger.shared.domain.model.VoteOption

class MockVoteApi: VoteApi {
    private val testPlaylistId = "26YTAFslmPya0jBIFSglKd"
    private val myUserId = "janajansen-de"

    private val user2Id = "user2"
    private val user3Id = "user3"

    private val dummyData = mutableListOf(
        VoteDto(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = myUserId,
            voteOption = VoteOption.KEEP
        ),
        VoteDto(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = user2Id,
            voteOption = VoteOption.KEEP
        ),
        VoteDto(
            playlistId = testPlaylistId,
            trackId = "5E3BK90EQQxmrDz3zLG2l9",
            userId = user3Id,
            voteOption = VoteOption.DONT_CARE
        ),
        VoteDto(
            playlistId = testPlaylistId,
            trackId = "0y8UKPyJOluqIuacosTKEv",
            userId = myUserId,
            voteOption = VoteOption.REMOVE
        ),
        VoteDto(
            playlistId = testPlaylistId,
            trackId = "0y8UKPyJOluqIuacosTKEv",
            userId = user2Id,
            voteOption = VoteOption.KEEP
        )
    )

    override fun getUsersVotesForPlaylist(playlistId: String, userId: String): Result<List<VoteDto>> {
        return Result.success(dummyData.filter { it.playlistId == playlistId && it.userId == userId })
    }

    // 572g
    override fun getAllVotesForPlaylist(playlistId: String): Result<List<VoteDto>> {
        return Result.success(dummyData.filter { it.playlistId == playlistId })
    }

    override fun upsertVote(
        playlistId: String,
        trackId: String,
        userId: String,
        newVote: VoteOption
    ): Result<Unit> {

        val oldVote =
            dummyData.firstOrNull { it.playlistId == playlistId && it.trackId == trackId && it.userId == userId }

        dummyData.remove(oldVote)

        dummyData.add(VoteDto(playlistId, trackId, userId, newVote))

        return Result.success(Unit)
    }

}