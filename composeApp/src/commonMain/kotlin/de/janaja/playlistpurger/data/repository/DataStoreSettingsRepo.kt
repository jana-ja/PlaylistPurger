package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.local.DataStorePreferences
import de.janaja.playlistpurger.data.local.DatastoreKeys
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import kotlinx.coroutines.flow.Flow

class DataStoreSettingsRepo(
    private val dataStorePreferences: DataStorePreferences
): SettingsRepo {

    override val showSwipeFirstFlow: Flow<Boolean?> = dataStorePreferences.getPreference(DatastoreKeys.showSwipeFirst, true)

    override suspend fun updateShowSwipeFirst(value: Boolean) {
        dataStorePreferences.putPreference(
            DatastoreKeys.showSwipeFirst,
            value
        )
    }
}