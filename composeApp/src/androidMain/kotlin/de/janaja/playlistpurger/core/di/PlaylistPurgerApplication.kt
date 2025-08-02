package de.janaja.playlistpurger.core.di

import android.app.Application
import de.janaja.playlistpurger.core.di.initKoin
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