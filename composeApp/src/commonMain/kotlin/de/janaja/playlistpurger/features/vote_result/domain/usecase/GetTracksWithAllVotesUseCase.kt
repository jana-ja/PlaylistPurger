package de.janaja.playlistpurger.features.vote_result.domain.usecase

import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTracksWithAllVotesUseCase(
    private val trackListRepo: TrackListRepo,
) {
    // this returns flow so result can be combined with filtering easily
    operator fun invoke(playlistId: String): Flow<Result<List<Pair<Track,List<Vote>>>>> = flow {
        val result = trackListRepo.loadTracksWithAllVotes(playlistId)
        emit(result)
    }
}