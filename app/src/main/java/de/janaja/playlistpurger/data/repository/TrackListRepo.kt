package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption
import kotlinx.coroutines.flow.Flow

interface TrackListRepo {

    val allTracks: Flow<List<Track>>

    val unvotedTracks: Flow<List<Track>>

    suspend fun loadTracksWithVotes(playlistId: String)

    // need newVote, trackId, playlistId, userId - where should this be stored?
    // inject current user id into repo? and token datastore thing?
    fun updateVote(playlistId: String, trackId: String, newVote: VoteOption)
}