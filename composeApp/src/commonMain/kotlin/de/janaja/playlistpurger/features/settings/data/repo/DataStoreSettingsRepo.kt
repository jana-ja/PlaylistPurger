package de.janaja.playlistpurger.features.settings.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import de.janaja.playlistpurger.features.settings.data.local.DatastoreKeys
import de.janaja.playlistpurger.features.settings.domain.Settings
import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreSettingsRepo(
    private val dataStore: DataStore<Preferences>
): SettingsRepo {

    override fun observeSettings(): Flow<Settings> {
        return dataStore.data.map { preferences ->
            Settings(
                showSwipeFirst = preferences[DatastoreKeys.showSwipeFirst] ?: true
            )
        }
    }
    override suspend fun updateShowSwipeFirst(value: Boolean) {
        // TODO error handling IO Exception
        dataStore.edit { preferences ->
            preferences[DatastoreKeys.showSwipeFirst]
        }
    }
}