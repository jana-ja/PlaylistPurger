package de.janaja.playlistpurger.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

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