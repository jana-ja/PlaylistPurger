package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.core.data.local.DataStoreFactory
import de.janaja.playlistpurger.core.data.remote.HttpClientFactory
import de.janaja.playlistpurger.features.auth.data.helper.SpotifyOAuthResponseHelper
import de.janaja.playlistpurger.features.auth.data.remote.KtorSpotifyAccountApi
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyAccountApi
import de.janaja.playlistpurger.features.auth.data.repo.DataStoreTokenRepo
import de.janaja.playlistpurger.features.auth.data.service.SpotifyAuthService
import de.janaja.playlistpurger.features.auth.domain.helper.OAuthResponseHelper
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.shared.data.remote.KtorSpotifyWebApi
import de.janaja.playlistpurger.shared.data.remote.MyVoteApi
import de.janaja.playlistpurger.shared.data.remote.SpotifyWebApi
import de.janaja.playlistpurger.shared.data.remote.VoteApi
import de.janaja.playlistpurger.shared.data.repo.SpotifyTrackListRepo
import de.janaja.playlistpurger.shared.domain.repository.TrackListRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val platformModule: Module

val coreModule = module {
    // HttpClientFactory gets module specific HttpClientEngine
    single(qualifier = named(DiNames.AUTHENTICATED_HTTP_CLIENT)) { HttpClientFactory.createAuthClient(get(), get()) }

    single(qualifier = named(DiNames.GENERAL_HTTP_CLIENT)) { HttpClientFactory.createGeneralClient(get()) }

    // Spotify WebApiService
    single<SpotifyWebApi> {
        KtorSpotifyWebApi(get(qualifier = named(DiNames.AUTHENTICATED_HTTP_CLIENT)))
    }

    // Spotify AccountApiService
    single<SpotifyAccountApi> {
        KtorSpotifyAccountApi(get(qualifier = named(DiNames.GENERAL_HTTP_CLIENT)))
    }

    // AuthRepo uses TokenRepo and WebApiService and AccountApiService
    single<AuthService> {
//        MockAuthService(true)
        SpotifyAuthService(get(), get())
    }

    // VoteRepo
    single<VoteApi> {
//        MockVoteApi()
        MyVoteApi(get(qualifier = named(DiNames.GENERAL_HTTP_CLIENT)))
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


    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    // TokenRepo
    single<TokenRepo> {
        DataStoreTokenRepo(get(), get(), get())
    }

    // LoginResponseHelper
    // gets created every time it is needed
    factory { CoroutineScope(Dispatchers.IO) }
    single<OAuthResponseHelper> {
        SpotifyOAuthResponseHelper(
            scope = get()
        )
    }
}
