package de.janaja.playlistpurger.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object DatastoreKeys {
    val accessToken by lazy { stringPreferencesKey("accessToken") }
}