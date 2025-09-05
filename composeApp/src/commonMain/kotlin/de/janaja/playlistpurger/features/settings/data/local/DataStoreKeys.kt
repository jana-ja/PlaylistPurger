package de.janaja.playlistpurger.features.settings.data.local

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DatastoreKeys {
    val accessToken by lazy { stringPreferencesKey("accessToken") }
    val refreshToken by lazy { stringPreferencesKey("refreshToken") }
    val showSwipeFirst by lazy { booleanPreferencesKey("showSwipeFirst") }
}



// Interface to help with generic access (optional but can be nice)
interface DatastoreEntryDefinition<T> {
    val key: Preferences.Key<T>
    val defaultValue: T
}

enum class DatastoreEntry {
    ACCESS_TOKEN {
        override val key = DatastoreKeys.accessToken
        override val defaultValue: String? = null // Or ""
    },
    REFRESH_TOKEN {
        override val key = DatastoreKeys.refreshToken
        override val defaultValue: String? = null // Or ""
    },
    SHOW_SWIPE_FIRST {
        override val key = DatastoreKeys.showSwipeFirst
        override val defaultValue: Boolean = true
    };

    // Abstract members to be implemented by each enum constant
    abstract val key: Preferences.Key<out Any?> // Use 'out Any?' for variance with specific key types
    abstract val defaultValue: Any?

    // Typed accessor for convenience, though direct access to key/defaultValue above is often fine
    @Suppress("UNCHECKED_CAST")
    fun <T> preferenceKey(): Preferences.Key<T> = key as Preferences.Key<T>

    @Suppress("UNCHECKED_CAST")
    fun <T> default(): T = defaultValue as T
}

// Helper extension function for Preferences
fun <T> Preferences.getOrDefault(setting: DatastoreEntry): T {
    return this[setting.preferenceKey<T>()] ?: setting.default<T>()
}
