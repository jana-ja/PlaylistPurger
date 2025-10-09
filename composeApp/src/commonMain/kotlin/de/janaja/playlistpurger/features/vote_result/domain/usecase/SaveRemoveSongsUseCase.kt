package de.janaja.playlistpurger.features.vote_result.domain.usecase

import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo

class SaveRemoveSongsUseCase(private val trackListRepo: TrackListRepo) {
    suspend operator fun invoke(playlistId: String, trackList: List<Track>): Result<Unit> {
        return trackListRepo.saveRemoveSongsToPlaylist(playlistId, trackList)
    }
}