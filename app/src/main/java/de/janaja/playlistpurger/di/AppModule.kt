package de.janaja.playlistpurger.di

import de.janaja.playlistpurger.data.repository.DataStoreRepo
import de.janaja.playlistpurger.data.repository.DataStoreRepoImpl
import de.janaja.playlistpurger.data.repository.TrackListRepo
import org.koin.dsl.module
import de.janaja.playlistpurger.data.repository.TrackListRepoImpl
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf


val appModule = module {

    // DataStoreRepo
    single<DataStoreRepo> {
        DataStoreRepoImpl(androidContext())
    }

    // TrackListRepo
//    singleOf(::TrackListRepoImpl)
    single<TrackListRepo> {
        TrackListRepoImpl(get())
    }

    // TrackListViewModel uses TrackListRepo and DataStoreRepo
    viewModelOf(::TrackListViewModel)


}
