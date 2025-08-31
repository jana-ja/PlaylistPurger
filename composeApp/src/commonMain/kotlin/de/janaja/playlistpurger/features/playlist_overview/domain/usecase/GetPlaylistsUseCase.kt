package de.janaja.playlistpurger.features.playlist_overview.domain.usecase

import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPlaylistsUseCase(
    private val playlistRepo: PlaylistRepo,
) {
    // this returns flow so result can be combined with filtering easily
    operator fun invoke(): Flow<Result<List<Playlist>>> = flow {
        emit(playlistRepo.getPlaylists())
    }
}