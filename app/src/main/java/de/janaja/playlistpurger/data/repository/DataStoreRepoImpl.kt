package de.janaja.playlistpurger.data.repository

import android.content.Context
import de.janaja.playlistpurger.DatastoreKeys
import de.janaja.playlistpurger.util.DataStorePreferences
import de.janaja.playlistpurger.util.SecurityUtil

class DataStoreRepoImpl(context: Context): DataStoreRepo {

    private val dataStorePreferences = DataStorePreferences(
        context,
        SecurityUtil()
    )

    override val tokenFlow = dataStorePreferences.getSecurePreference(DatastoreKeys.accessToken)
}