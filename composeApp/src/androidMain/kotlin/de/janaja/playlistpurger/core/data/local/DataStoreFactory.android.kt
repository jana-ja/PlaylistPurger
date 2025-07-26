package de.janaja.playlistpurger.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import de.janaja.playlistpurger.features.settings.data.local.createDataStore
import de.janaja.playlistpurger.features.settings.data.local.dataStoreFileName

actual class DataStoreFactory(
    private val context: Context
) {
    actual fun create(): DataStore<Preferences> {
//        return createDataStore(context)
        return createDataStore(
            producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
        )
    }
}