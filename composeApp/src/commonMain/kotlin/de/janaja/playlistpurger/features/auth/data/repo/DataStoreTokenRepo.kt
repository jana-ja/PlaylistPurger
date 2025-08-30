package de.janaja.playlistpurger.features.auth.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import de.janaja.playlistpurger.features.settings.data.local.DatastoreKeys
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DataStoreTokenRepo(
    private val dataStore: DataStore<Preferences>,
    private val externalScope: CoroutineScope
) : TokenRepo {

    override val accessTokenFlow = dataStore.data.map { preferences ->
        preferences[DatastoreKeys.accessToken]
    }.map { token ->
        if (token != null)
            TokenState.Loaded(token)
        else
            TokenState.NoToken
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TokenState.Loading
    )

    override val refreshTokenFlow = dataStore.data.map { preferences ->
        preferences[DatastoreKeys.refreshToken]
    }.map { token ->
        if (token != null)
            TokenState.Loaded(token)
        else
            TokenState.NoToken
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TokenState.Loading
    )

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