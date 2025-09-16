package de.janaja.playlistpurger.features.track_voting.di

import de.janaja.playlistpurger.features.track_voting.domain.usecase.AdjustPlaybackPositionUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.GetAvailableDevicesUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.ObserveTracksWithOwnVotesUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.PauseUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.PlayTrackUseCase
import de.janaja.playlistpurger.features.track_voting.domain.usecase.UpsertVoteUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val trackVotingModule = module {
    factoryOf(::ObserveTracksWithOwnVotesUseCase)
    factoryOf(::UpsertVoteUseCase)
    factoryOf(::GetAvailableDevicesUseCase)
    factoryOf(::PlayTrackUseCase)
    factoryOf(::PauseUseCase)
    factoryOf(::AdjustPlaybackPositionUseCase)
}