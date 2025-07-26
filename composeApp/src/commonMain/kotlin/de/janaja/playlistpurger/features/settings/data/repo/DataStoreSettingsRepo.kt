package de.janaja.playlistpurger.features.settings.data.repo

import de.janaja.playlistpurger.features.settings.data.local.DataStorePreferences
import de.janaja.playlistpurger.features.settings.data.local.DatastoreKeys
import de.janaja.playlistpurger.features.settings.domain.repo.SettingsRepo
import kotlinx.coroutines.flow.Flow

class DataStoreSettingsRepo(
    private val dataStorePreferences: DataStorePreferences
): SettingsRepo {

    override val showSwipeFirstFlow: Flow<Boolean?> = dataStorePreferences.getPreference(
        DatastoreKeys.showSwipeFirst, true)

    override suspend fun updateShowSwipeFirst(value: Boolean) {
        dataStorePreferences.putPreference(
            DatastoreKeys.showSwipeFirst,
            value
        )
    }
}