package de.janaja.playlistpurger.features.auth.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import de.janaja.playlistpurger.features.settings.data.local.DatastoreKeys
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import kotlinx.coroutines.flow.map

class DataStoreTokenRepo(
    private val dataStore: DataStore<Preferences>
): TokenRepo {

    override val accessTokenFlow = dataStore.data.map { preferences ->
        preferences[DatastoreKeys.accessToken]
    }

    override val refreshTokenFlow = dataStore.data.map { preferences ->
        preferences[DatastoreKeys.refreshToken]
    }

    override suspend fun updateAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[DatastoreKeys.accessToken] = token
        }
    }

    override suspend fun updateRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[DatastoreKeys.refreshToken] = token
        }
    }

    override suspend fun deleteAllToken() {
        dataStore.edit { preferences ->
            preferences.remove(DatastoreKeys.accessToken)
            preferences.remove(DatastoreKeys.refreshToken)
        }
    }
}