package de.janaja.playlistpurger.features.playlist_overview.di

import de.janaja.playlistpurger.features.playlist_overview.data.repo.SpotifyPlaylistRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.usecase.GetPlaylistsUseCase
import de.janaja.playlistpurger.shared.data.repo.SpotifyUserRepo
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val playlistOverviewModule = module {
    single<UserRepo> {
        SpotifyUserRepo(get(), get(), get())
    }

    single<PlaylistRepo> {
//        MockPlaylistRepo(true)
        SpotifyPlaylistRepo(get(), get())
    }

    factoryOf(::GetPlaylistsUseCase)
}