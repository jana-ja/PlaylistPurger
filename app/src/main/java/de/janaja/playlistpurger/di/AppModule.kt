package de.janaja.playlistpurger.di

import com.spotify.sdk.android.auth.AuthorizationRequest
import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.DataStoreRepoImpl
import de.janaja.playlistpurger.data.repository.PlaylistRepo
import de.janaja.playlistpurger.data.repository.PlaylistRepoImpl
import de.janaja.playlistpurger.data.repository.TrackListRepo
import org.koin.dsl.module
import de.janaja.playlistpurger.data.repository.TrackListRepoImpl
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

    // AuthViewModel uses DataStoreRepo
    viewModel {
            (onStartLoginActivity: (AuthorizationRequest) -> Unit) -> // Define the callback as a parameter
        AuthViewModel(get(), onStartLoginActivity)
    }

    // TrackListRepo uses DataStoreRepo
//    singleOf(::TrackListRepoImpl)
    single<TrackListRepo> {
        TrackListRepoImpl(get())
    }

    // TrackListViewModel uses TrackListRepo and DataStoreRepo
    viewModelOf(::TrackListViewModel)

    // PlayListRepo uses DataStoreRepo
    single<PlaylistRepo> {
        PlaylistRepoImpl(get())
    }

    // PlayListViewModel uses PlayListRepo and DataStoreRepo
    viewModelOf(::PlaylistOverviewViewModel)


}
