package de.janaja.playlistpurger.domain.repository

import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption
import kotlinx.coroutines.flow.Flow

interface TrackListRepo {

    suspend fun loadTracksWithOwnVotes(playlistId: String): Result<Flow<List<Track>>>

    // need newVote, trackId, playlistId, userId - where should this be stored?
    // inject current user id into repo? and token datastore thing?
    suspend fun updateVote(playlistId: String, trackId: String, newVote: VoteOption): Result<Unit>

    suspend fun loadTracksWithAllVotes(playlistId: String): Result<List<Pair<Track, List<Vote>>>>
}