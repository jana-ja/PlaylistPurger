package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.remote.spotify.model.TrackDto
import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption
import kotlinx.coroutines.flow.Flow

interface TrackListRepo {

    val allTracks: Flow<List<TrackDto>>

    suspend fun loadTracksWithOwnVotes(playlistId: String)

    // need newVote, trackId, playlistId, userId - where should this be stored?
    // inject current user id into repo? and token datastore thing?
    fun updateVote(playlistId: String, trackId: String, newVote: VoteOption)

    suspend fun loadTracksWithAllVotes(playlistId: String): List<Pair<TrackDto, List<Vote>>>
}