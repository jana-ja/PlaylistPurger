package de.janaja.playlistpurger.data.repository

import android.content.Context
import de.janaja.playlistpurger.data.local.DataStorePreferences
import de.janaja.playlistpurger.data.local.DatastoreKeys
import de.janaja.playlistpurger.domain.repository.TokenRepo
import de.janaja.playlistpurger.data.local.SecurityUtil

class DataStoreTokenRepo(context: Context): TokenRepo {

    private val dataStorePreferences = DataStorePreferences(
        context,
        SecurityUtil()
    )

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