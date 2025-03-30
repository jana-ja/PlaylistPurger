package de.janaja.playlistpurger.data.repository

import android.content.Context
import com.spotify.sdk.android.auth.AuthorizationResponse
import de.janaja.playlistpurger.data.local.DataStorePreferences
import de.janaja.playlistpurger.data.local.DatastoreKeys
import de.janaja.playlistpurger.util.SecurityUtil

class DataStoreRepoImpl(context: Context): DataStoreRepo {

    private val dataStorePreferences = DataStorePreferences(
        context,
        SecurityUtil()
    )

    override val tokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.accessToken)

    override suspend fun updateToken(token: String) {
        dataStorePreferences.putSecurePreference(
            DatastoreKeys.accessToken,
            token
        )
        println("Success! ${AuthorizationResponse.Type.TOKEN}")
    }

    override suspend fun deleteToken() {
        dataStorePreferences.removePreference(DatastoreKeys.accessToken)
    }
}