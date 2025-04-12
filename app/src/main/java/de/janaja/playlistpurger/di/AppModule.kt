package de.janaja.playlistpurger.di

import com.spotify.sdk.android.auth.AuthorizationRequest
import de.janaja.playlistpurger.domain.repository.TokenRepo
import de.janaja.playlistpurger.data.repository.DataStoreTokenRepo
import de.janaja.playlistpurger.domain.repository.PlaylistRepo
import de.janaja.playlistpurger.data.repository.SpotifyPlaylistRepo
import de.janaja.playlistpurger.domain.repository.TrackListRepo
import org.koin.dsl.module
import de.janaja.playlistpurger.data.repository.SpotifyTrackListRepo
import de.janaja.playlistpurger.data.remote.vote.VoteApi
import de.janaja.playlistpurger.data.remote.vote.VoteApiDummyImpl
import de.janaja.playlistpurger.data.repository.DataStoreSettingsRepo
import de.janaja.playlistpurger.data.repository.SpotifyAuthRepo
import de.janaja.playlistpurger.domain.repository.AuthRepo
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import de.janaja.playlistpurger.ui.viewmodel.TrackListVoteViewModel
import de.janaja.playlistpurger.ui.viewmodel.VoteResultViewModel
import de.janaja.playlistpurger.ui.viewmodel.PlaylistOverviewViewModel
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import de.janaja.playlistpurger.ui.viewmodel.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf


val appModule = module {

    // TokenRepo
    single<TokenRepo> {
        DataStoreTokenRepo(androidContext())
    }

    // SettingsRepo
    single<SettingsRepo> {
        DataStoreSettingsRepo(androidContext())
    }

    single<AuthRepo> {
        SpotifyAuthRepo(get())
    }

    // VoteRepo
    single<VoteApi> {
        VoteApiDummyImpl()
    }

    // PlayListRepo uses DataStoreRepo
    single<PlaylistRepo> {
        SpotifyPlaylistRepo(get())
    }

    // TrackListRepo uses DataStoreRepo and VoteRepo
//    singleOf(::TrackListRepoImpl)
    single<TrackListRepo> {
        SpotifyTrackListRepo(get(), get())
    }

    // AuthViewModel uses DataStoreRepo
    viewModel { (onStartLoginActivity: (AuthorizationRequest) -> Unit) ->
        AuthViewModel(get(), onStartLoginActivity)
    }

    // PlayListViewModel uses PlayListRepo and DataStoreRepo
    viewModelOf(::PlaylistOverviewViewModel)

    // TrackListViewModel uses VoteRepo and TrackListRepo and DataStoreRepo
    viewModelOf(::TrackListVoteViewModel)

    // VoteResultViewModel uses TrackListRepo
    viewModelOf(::VoteResultViewModel)

    // SettingsViewModel uses SettingsRepo
    viewModelOf(::SettingsViewModel)
}
