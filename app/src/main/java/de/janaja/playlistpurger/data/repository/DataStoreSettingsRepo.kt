package de.janaja.playlistpurger.data.repository

import android.content.Context
import de.janaja.playlistpurger.data.local.DataStorePreferences
import de.janaja.playlistpurger.data.local.DatastoreKeys
import de.janaja.playlistpurger.domain.repository.SettingsRepo
import de.janaja.playlistpurger.data.local.SecurityUtil
import kotlinx.coroutines.flow.Flow

class DataStoreSettingsRepo(context: Context): SettingsRepo {

    private val dataStorePreferences = DataStorePreferences(
        context,
        SecurityUtil()
    )

    override val showSwipeFirstFlow: Flow<Boolean?> = dataStorePreferences.getPreference(DatastoreKeys.showSwipeFirst, true)

    override suspend fun updateShowSwipeFirst(value: Boolean) {
        dataStorePreferences.putPreference(
            DatastoreKeys.showSwipeFirst,
            value
        )
    }
}