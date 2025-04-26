package de.janaja.playlistpurger.di

import de.janaja.playlistpurger.data.local.DataStoreFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule = module {

    // TODO
    single<HttpClientEngine> { Darwin.create() }

    // DataStore - module specific
    single {
        get<DataStoreFactory>().create()
    }
}
