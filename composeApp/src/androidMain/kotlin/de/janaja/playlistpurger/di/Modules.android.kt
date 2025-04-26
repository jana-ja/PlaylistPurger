package de.janaja.playlistpurger.di

import de.janaja.playlistpurger.data.local.DataStoreFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module //= module {
    get() = module {
    // TODO
    single<HttpClientEngine> { OkHttp.create() }

    // DataStore - module specific
    single {
        DataStoreFactory(androidContext()).create()
    }

}
