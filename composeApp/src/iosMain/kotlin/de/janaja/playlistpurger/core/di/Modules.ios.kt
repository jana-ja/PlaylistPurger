package de.janaja.playlistpurger.core.di

import de.janaja.playlistpurger.core.data.local.DataStoreFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule = module {

    // TODO
    single<HttpClientEngine> { Darwin.create() }

    // DataStore - module specific
    single {
        DataStoreFactory().create()
    }
}
