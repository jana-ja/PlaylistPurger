package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.core.data.local.DataStoreFactory
import de.janaja.playlistpurger.core.data.remote.HttpClientFactory
import de.janaja.playlistpurger.core.domain.usecase.ExecuteAuthenticatedRequestUseCase
import de.janaja.playlistpurger.features.auth.data.helper.SpotifyOAuthResponseHelper
import de.janaja.playlistpurger.features.auth.data.remote.KtorSpotifyAccountApi
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyAccountApi
import de.janaja.playlistpurger.features.auth.data.repo.DataStoreTokenRepo
import de.janaja.playlistpurger.features.auth.data.service.SpotifyAuthService
import de.janaja.playlistpurger.features.auth.domain.helper.OAuthResponseHelper
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.settings.data.local.DataStorePreferences
import de.janaja.playlistpurger.shared.data.remote.KtorSpotifyWebApi
import de.janaja.playlistpurger.shared.data.remote.MockVoteApi
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.data.remote.VoteApi
import de.janaja.playlistpurger.shared.data.repo.SpotifyTrackListRepo
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val coreModule = module {
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

    // LoginResponseHelper
    factory { CoroutineScope(Dispatchers.IO) }
    single<OAuthResponseHelper> {
        SpotifyOAuthResponseHelper(
            scope = get()
        )
    }

    // gets created every time it is needed
    factoryOf(::ExecuteAuthenticatedRequestUseCase)

}
