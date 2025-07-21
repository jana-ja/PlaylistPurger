package de.janaja.playlistpurger.di

//import com.spotify.sdk.android.auth.AuthorizationRequest
import de.janaja.playlistpurger.data.local.DataStoreFactory
import de.janaja.playlistpurger.data.local.DataStorePreferences
//import de.janaja.playlistpurger.data.local.SecurityUtil
import de.janaja.playlistpurger.data.remote.HttpClientFactory
import de.janaja.playlistpurger.data.remote.spotify.KtorSpotifyAccountApiService
import de.janaja.playlistpurger.data.remote.spotify.KtorSpotifyWebApiService
import de.janaja.playlistpurger.data.remote.spotify.SpotifyAccountApiService
import de.janaja.playlistpurger.data.remote.spotify.SpotifyWebApiService
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
import de.janaja.playlistpurger.data.repository.SpotifyAuthService
import de.janaja.playlistpurger.data.repository.mock.MockAuthService
import de.janaja.playlistpurger.data.repository.mock.MockPlaylistRepo
import de.janaja.playlistpurger.domain.repository.AuthService
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import de.janaja.playlistpurger.ui.viewmodel.TrackListVoteViewModel
import de.janaja.playlistpurger.ui.viewmodel.VoteResultViewModel
import de.janaja.playlistpurger.ui.viewmodel.PlaylistOverviewViewModel
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import de.janaja.playlistpurger.ui.viewmodel.SettingsViewModel
import de.janaja.playlistpurger.util.LoginResponseHelper
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
    singleOf(::KtorSpotifyWebApiService).bind<SpotifyWebApiService>()

    // Spotify AccountApiService
    singleOf(::KtorSpotifyAccountApiService).bind<SpotifyAccountApiService>()

    // AuthRepo uses TokenRepo and WebApiService and AccountApiService
    single<AuthService> {
//        MockAuthService(true)
        SpotifyAuthService(get(), get(), get())
    }

    // VoteRepo
    single<VoteApi> {
        VoteApiDummyImpl()
    }

    // PlayListRepo uses DataStoreRepo and WebApiService
    single<PlaylistRepo> {
//        MockPlaylistRepo(true)
        SpotifyPlaylistRepo(get(), get())
    }

    // TrackListRepo uses DataStoreRepo and VoteRepo and WebApiService
//    singleOf(::TrackListRepoImpl)
    single<TrackListRepo> {
        SpotifyTrackListRepo(get(), get(), get())
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
    single { 
        LoginResponseHelper(
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
