package de.janaja.playlistpurger.features.track_voting.domain.usecase

import de.janaja.playlistpurger.core.domain.usecase.ExecuteAuthenticatedRequestUseCase
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ObserveTracksWithOwnVotesUseCase(
    private val trackListRepo: TrackListRepo,
    private val executeAuthenticatedRequestUseCase: ExecuteAuthenticatedRequestUseCase
) {
    operator fun invoke(playlistId: String): Flow<Result<List<Track>>> = flow {
        Log.d("ObserveTracksWithOwnVotes", "invoke")
        // try refresh/load TracksWithOwnVotes
        val result = executeAuthenticatedRequestUseCase {
            trackListRepo.loadCurrentPlaylistTracks(playlistId)
        }

        // if failure then stop
        result.onFailure {
            emit(Result.failure(it))
            return@flow
        }

        // if success then re-emit internal StateFlow of repo
        trackListRepo.observeCurrentPlaylistTracks()
            .map { Result.success(it) }
            .collect { emit(it) }

    }
}