package de.janaja.playlistpurger.data.repository

import de.janaja.playlistpurger.data.local.DataStorePreferences
import de.janaja.playlistpurger.data.local.DatastoreKeys
import de.janaja.playlistpurger.domain.repository.TokenRepo

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