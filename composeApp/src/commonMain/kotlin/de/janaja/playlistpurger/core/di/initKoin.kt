package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.features.auth.di.authModule
import de.janaja.playlistpurger.features.playlist_overview.di.playlistOverviewModule
import de.janaja.playlistpurger.features.settings.di.settingsModule
import de.janaja.playlistpurger.features.track_voting.di.trackVotingModule
import de.janaja.playlistpurger.features.vote_result.di.voteResultModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreModule,
            viewModelModule,
            authModule,
            playlistOverviewModule,
            settingsModule,
            trackVotingModule,
            voteResultModule,
            platformModule // must come last
        )
    }
}