package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.model.Track

interface TrackListRepo {

    suspend fun getTracks(playlistId: String): List<Track>

    // need newVote, trackId, playlistId, userId - where should this be stored?
    // inject current user id into repo? and token datastore thing?
    fun updateVote(track: Track, playlistId: String)
}