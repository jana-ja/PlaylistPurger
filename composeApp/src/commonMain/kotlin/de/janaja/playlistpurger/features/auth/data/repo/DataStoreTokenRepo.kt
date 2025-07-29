package de.janaja.playlistpurger.features.auth.data.repo

import de.janaja.playlistpurger.features.settings.data.local.DataStorePreferences
import de.janaja.playlistpurger.features.settings.data.local.DatastoreKeys
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo

// TODO update this remove dataStorePreferences
class DataStoreTokenRepo(
    private val dataStorePreferences: DataStorePreferences
): TokenRepo {

    override val accessTokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.accessToken)
    override val refreshTokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.refreshToken)

    override suspend fun updateAccessToken(token: String) {
        dataStorePreferences.putSecurePreference(
            DatastoreKeys.accessToken,
            token
        )
    }

    override suspend fun updateRefreshToken(token: String) {
        dataStorePreferences.putSecurePreference(
            DatastoreKeys.refreshToken,
            token
        )
    }

    override suspend fun deleteAllToken() {
        dataStorePreferences.removePreference(DatastoreKeys.accessToken)
        dataStorePreferences.removePreference(DatastoreKeys.refreshToken)
    }
}