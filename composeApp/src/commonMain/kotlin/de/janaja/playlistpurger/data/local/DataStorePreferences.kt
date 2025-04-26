package de.janaja.playlistpurger.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
// https://iamraghavawasthi.medium.com/securing-your-android-datastore-4c50f3e98d5c
class DataStorePreferences(
//    private val securityUtil: SecurityUtil,
    private val dataStore: DataStore<Preferences>
) {

    private val bytesToStringSeperator = "|"
    private val keyAlias = "appkey"
    private val ivToStringSeparator= ":iv:"

    fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
            Flow<T> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[key] ?: defaultValue
        result
    }


    suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun putSecurePreference(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            // TODO add security back
//            val (iv, secureByteArray) = securityUtil.encryptData(keyAlias, value)
//            val secureString = iv.joinToString(bytesToStringSeperator) + ivToStringSeparator + secureByteArray.joinToString(bytesToStringSeperator)
//            preferences[key] = secureString
            preferences[key] = value
        }
    }

    fun getSecurePreference(
        key: Preferences.Key<String>
    ):
            Flow<String?> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val secureString = preferences[key] ?: return@map null
        // TODO add security back
//        val (ivString, encryptedString) = secureString.split(ivToStringSeparator, limit = 2)
//        val iv = ivString.split(bytesToStringSeperator).map { it.toByte() }.toByteArray()
//        val encryptedData = encryptedString.split(bytesToStringSeperator).map { it.toByte() }.toByteArray()
//        val decryptedValue = securityUtil.decryptData(keyAlias, iv, encryptedData)
//        decryptedValue
        secureString

    }


    suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}