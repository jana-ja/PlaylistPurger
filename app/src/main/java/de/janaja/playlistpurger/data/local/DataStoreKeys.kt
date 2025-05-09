package de.janaja.playlistpurger.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DatastoreKeys {
    val accessToken by lazy { stringPreferencesKey("accessToken") }
    val refreshToken by lazy { stringPreferencesKey("refreshToken") }
    val showSwipeFirst by lazy { booleanPreferencesKey("showSwipeFirst") }
}