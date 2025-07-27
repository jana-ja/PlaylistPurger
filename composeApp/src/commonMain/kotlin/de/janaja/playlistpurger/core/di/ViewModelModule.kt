package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.features.auth.presentation.viewmodel.AuthViewModel
import de.janaja.playlistpurger.features.playlist_overview.presentation.viewmodel.PlaylistOverviewViewModel
import de.janaja.playlistpurger.features.settings.presentation.viewmodel.SettingsViewModel
import de.janaja.playlistpurger.features.track_voting.presentation.viewmodel.TrackListVoteViewModel
import de.janaja.playlistpurger.features.vote_result.presentation.viewmodel.VoteResultViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// TODO find right place
val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::TrackListVoteViewModel)
    viewModelOf(::VoteResultViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::PlaylistOverviewViewModel)
}
