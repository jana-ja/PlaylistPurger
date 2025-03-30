package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption

interface TrackListRepo {

    suspend fun getTracksWithVotes(playlistId: String): List<Track>

    // need newVote, trackId, playlistId, userId - where should this be stored?
    // inject current user id into repo? and token datastore thing?
    fun updateVote(playlistId: String, trackId: String, newVote: VoteOption)
}