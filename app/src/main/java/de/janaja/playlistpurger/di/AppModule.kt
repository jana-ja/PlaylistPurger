package de.janaja.playlistpurger.di

import com.spotify.sdk.android.auth.AuthorizationRequest
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.DataStoreRepoImpl
import de.janaja.playlistpurger.data.repository.PlaylistRepo
import de.janaja.playlistpurger.data.repository.PlaylistRepoImpl
import de.janaja.playlistpurger.data.repository.TrackListRepo
import org.koin.dsl.module
import de.janaja.playlistpurger.data.repository.TrackListRepoImpl
import de.janaja.playlistpurger.data.remote.VoteApi
import de.janaja.playlistpurger.data.remote.VoteApiDummyImpl
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel
import de.janaja.playlistpurger.ui.viewmodel.PlaylistOverviewViewModel
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf


val appModule = module {

    // DataStoreRepo
    single<DataStoreRepo> {
        DataStoreRepoImpl(androidContext())
    }

    // VoteRepo uses DataStoreRepo
    single<VoteApi> {
        VoteApiDummyImpl(get())
    }

    // PlayListRepo uses DataStoreRepo
    single<PlaylistRepo> {
        PlaylistRepoImpl(get())
    }

    // TrackListRepo uses DataStoreRepo and VoteRepo
//    singleOf(::TrackListRepoImpl)
    single<TrackListRepo> {
        TrackListRepoImpl(get(), get())
    }

    // AuthViewModel uses DataStoreRepo
    viewModel { (onStartLoginActivity: (AuthorizationRequest) -> Unit) ->
        AuthViewModel(get(), onStartLoginActivity)
    }

    // PlayListViewModel uses PlayListRepo and DataStoreRepo
    viewModelOf(::PlaylistOverviewViewModel)

    // TrackListViewModel uses VoteRepo and TrackListRepo and DataStoreRepo
    viewModelOf(::TrackListViewModel)
}
