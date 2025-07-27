package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.core.data.local.DataStoreFactory
import de.janaja.playlistpurger.features.settings.data.local.DataStorePreferences
import de.janaja.playlistpurger.core.data.remote.HttpClientFactory
import de.janaja.playlistpurger.features.auth.data.remote.KtorSpotifyAccountApi
import de.janaja.playlistpurger.shared.data.remote.KtorSpotifyWebApi
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyAccountApi
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.auth.data.repo.DataStoreTokenRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.repo.PlaylistRepo
import de.janaja.playlistpurger.features.playlist_overview.data.repo.SpotifyPlaylistRepo
import org.koin.dsl.module
import de.janaja.playlistpurger.shared.data.repo.SpotifyTrackListRepo
import de.janaja.playlistpurger.shared.data.remote.VoteApi
import de.janaja.playlistpurger.shared.data.remote.MockVoteApi
import de.janaja.playlistpurger.features.settings.data.repo.DataStoreSettingsRepo
import de.janaja.playlistpurger.features.auth.data.service.SpotifyAuthService
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo
import de.janaja.playlistpurger.features.track_voting.presentation.viewmodel.TrackListVoteViewModel
import de.janaja.playlistpurger.features.vote_result.presentation.viewmodel.VoteResultViewModel
import de.janaja.playlistpurger.features.playlist_overview.presentation.viewmodel.PlaylistOverviewViewModel
import de.janaja.playlistpurger.features.auth.presentation.viewmodel.AuthViewModel
import de.janaja.playlistpurger.features.settings.presentation.viewmodel.SettingsViewModel
import de.janaja.playlistpurger.features.auth.data.helper.SpotifyOAuthResponseHelper
import de.janaja.playlistpurger.features.auth.domain.helper.OAuthResponseHelper
import de.janaja.playlistpurger.shared.data.repo.SpotifyUserRepo
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import de.janaja.playlistpurger.shared.domain.repository.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind

expect val platformModule: Module

val sharedModule = module {
    // HttpClientFactory gets module specific HttpClientEngine
    single { HttpClientFactory.create(get()) }

    // Spotify WebApiService
    singleOf(::KtorSpotifyWebApi).bind<SpotifyWebApi>()

    // Spotify AccountApiService
    singleOf(::KtorSpotifyAccountApi).bind<SpotifyAccountApi>()

    // AuthRepo uses TokenRepo and WebApiService and AccountApiService
    single<AuthService> {
//        MockAuthService(true)
        SpotifyAuthService(get(), get(), get())
    }

    // VoteRepo
    single<VoteApi> {
        MockVoteApi()
    }

    // PlaylistRepo
    single<UserRepo> {
        SpotifyUserRepo(get(), get())
    }

    // PlayListRepo uses DataStoreRepo and WebApiService
    single<PlaylistRepo> {
//        MockPlaylistRepo(true)
        SpotifyPlaylistRepo(get(), get(), get())
    }

    // TrackListRepo uses DataStoreRepo and VoteRepo and WebApiService
//    singleOf(::TrackListRepoImpl)
    single<TrackListRepo> {
        SpotifyTrackListRepo(get(), get(), get(), get())
    }

    // DataStore - module specific
    single {
        get<DataStoreFactory>().create()
    }

    // DataStorePreferences uses DataStore
    single {
        DataStorePreferences(
//            SecurityUtil(),
            get()
        )
    }
    // TokenRepo
    single<TokenRepo> {
        DataStoreTokenRepo(get())
    }

    // SettingsRepo
    single<SettingsRepo> {
        DataStoreSettingsRepo(get())
    }

    // LoginResponseHelper
    factory { CoroutineScope(Dispatchers.IO) }
    single<OAuthResponseHelper> {
        SpotifyOAuthResponseHelper(
            scope = get()
        )
    }

    // AuthViewModel uses DataStoreRepo
    viewModel { //(onStartLoginActivity: (AuthorizationRequest) -> Unit) ->
        AuthViewModel(get(), get())//, onStartLoginActivity)
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
