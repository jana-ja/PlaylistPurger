package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.features.auth.di.authModule
import de.janaja.playlistpurger.features.playlist_overview.di.playlistOverviewModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreModule,
            playlistOverviewModule,
            viewModelModule,
            authModule,
            platformModule // must come last
        )
    }
}