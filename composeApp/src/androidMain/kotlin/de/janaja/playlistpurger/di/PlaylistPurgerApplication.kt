package de.janaja.playlistpurger.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlaylistPurgerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@PlaylistPurgerApplication)
        }
    }

}