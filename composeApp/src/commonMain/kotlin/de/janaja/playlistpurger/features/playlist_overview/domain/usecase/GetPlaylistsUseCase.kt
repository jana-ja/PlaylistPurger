package de.janaja.playlistpurger.features.playlist_overview.domain.usecase

import de.janaja.playlistpurger.core.domain.usecase.ExecuteAuthenticatedRequestUseCase
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPlaylistsUseCase(
    private val playlistRepo: PlaylistRepo,
    private val executeAuthenticatedRequestUseCase: ExecuteAuthenticatedRequestUseCase
) {
    operator fun invoke(): Flow<Result<List<Playlist>>> = flow {
        val result = executeAuthenticatedRequestUseCase {
            playlistRepo.getPlaylists()
        }
        emit(result)
    }
}