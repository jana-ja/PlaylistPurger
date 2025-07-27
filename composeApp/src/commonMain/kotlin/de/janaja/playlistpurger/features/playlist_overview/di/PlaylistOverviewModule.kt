package de.janaja.playlistpurger.features.playlist_overview.di

import de.janaja.playlistpurger.features.playlist_overview.domain.usecase.GetPlaylistsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


val playlistOverviewModule = module {
    factoryOf(::GetPlaylistsUseCase)
}