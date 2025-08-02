package de.janaja.playlistpurger.shared.domain.repository

import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import kotlinx.coroutines.flow.Flow

interface TrackListRepo {
    /**
     * Loads (or retrieves from cache) the tracks and associated user votes for the specified [playlistId].
     * Upon successful loading, the tracks will be emitted by the flow returned from
     * [observeCurrentPlaylistTracks].
     *
     * @param playlistId The ID of the playlist to load and make current.
     * @return [Result.success] if tracks were successfully loaded/retrieved, [Result.failure] otherwise.
     */
    suspend fun loadCurrentPlaylistTracks(playlistId: String): Result<Unit>

    /**
     * Observes the tracks of the playlist most recently loaded via [loadCurrentPlaylistTracks].
     * The flow will emit an updated list of tracks whenever the current playlist's data changes
     * or when a new playlist is loaded.
     */
    fun observeCurrentPlaylistTracks(): Flow<List<Track>>

    // need newVote, trackId, playlistId, userId - where should this be stored?
    // inject current user id into repo? and token datastore thing?
    suspend fun upsertVote(playlistId: String, trackId: String, newVote: VoteOption): Result<Unit>

    suspend fun loadTracksWithAllVotes(playlistId: String): Result<List<Pair<Track, List<Vote>>>>
}