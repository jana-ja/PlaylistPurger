package de.janaja.playlistpurger.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import de.janaja.playlistpurger.features.settings.data.local.createDataStore
import de.janaja.playlistpurger.features.settings.data.local.dataStoreFileName
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class DataStoreFactory {
    @OptIn(ExperimentalForeignApi::class)
    actual fun create(): DataStore<Preferences> {
        return createDataStore(
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                requireNotNull(documentDirectory).path + "/$dataStoreFileName"
            }
        )
    }
}