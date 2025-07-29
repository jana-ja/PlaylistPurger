package de.janaja.playlistpurger.features.track_voting.domain.usecase

import de.janaja.playlistpurger.core.domain.usecase.ExecuteAuthenticatedRequestUseCase
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo

class UpsertVoteUseCase(
    private val trackListRepo: TrackListRepo,
    private val executeAuthenticatedRequestUseCase: ExecuteAuthenticatedRequestUseCase
) {
    suspend operator fun invoke(playlistId: String, trackId: String, newVote: VoteOption): Result<Unit> {
        return executeAuthenticatedRequestUseCase {
            trackListRepo.upsertVote(playlistId, trackId, newVote)
        }
    }
}