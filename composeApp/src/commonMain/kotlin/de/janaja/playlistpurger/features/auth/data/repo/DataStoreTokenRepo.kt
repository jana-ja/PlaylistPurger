package de.janaja.playlistpurger.features.auth.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyAccountApi
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyClientCredentials
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyClientCredentials.clientId
import de.janaja.playlistpurger.features.auth.data.remote.SpotifyClientCredentials.clientSecret
import de.janaja.playlistpurger.features.settings.data.local.DatastoreKeys
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.io.IOException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class DataStoreTokenRepo(
    private val dataStore: DataStore<Preferences>,
    private val accountApi: SpotifyAccountApi,
    externalScope: CoroutineScope,
) : TokenRepo {

    companion object {
        private const val TAG = "DataStoreTokenRepo"
    }

    private val spotifyClientCredentials = SpotifyClientCredentials

    override val accessTokenFlow = dataStore.data.map { preferences ->
        preferences[DatastoreKeys.accessToken]
    }.map { token ->
        if (token != null)
            TokenState.HasToken(token)
        else
            TokenState.NoToken
    }.onEach {
        Log.d(TAG, "accessTokenFlow: $it")
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TokenState.Loading
    )

    override val refreshTokenFlow = dataStore.data.map { preferences ->
        preferences[DatastoreKeys.refreshToken]
    }.map { token ->
        if (token != null)
            TokenState.HasToken(token)
        else
            TokenState.NoToken
    }.stateIn(
        scope = externalScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TokenState.Loading
    )

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun exchangeAuthCodeForTokens(code: String): Result<Unit> {
        // TODO loginState -> loading
        Log.d(TAG, "exchangeAuthCodeForTokens: try receive token for code: $code")

        val result = this.accountApi.getToken(
            client = "Basic " + Base64.encode("${spotifyClientCredentials.clientId}:${spotifyClientCredentials.clientSecret}".encodeToByteArray()),
            code = code,
            redirectUri = spotifyClientCredentials.redirectUri,
        )
        return result.fold(
            onSuccess = { tokenRequestResponse ->
                Log.d(TAG, "exchangeAuthCodeForTokens: got token for code")
                saveAccessToken(tokenRequestResponse.accessToken)
                saveRefreshToken(tokenRequestResponse.refreshToken)
                Result.success(Unit)
            },
            onFailure = { e ->
                Log.e(TAG, "exchangeAuthCodeForTokens: ", e)
                Result.failure(e)
            }
        )
    }


    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun refreshAccessToken(): Result<String> {
        // gets called from HttpClient Auth Plugin
        // successful -> get fresh accessToken from account, save it to datastore, StateFlow gets refreshed automatically, return new token
        // unsuccessful
        //     -> if refresh fails because of invalid refresh token -> no chance of recovering, clearAllTokens, StateFlow gets updated automatically, LoginState og App in AuthService will update automatically
        //     -> if refresh fails because of other reasons (no internet connection) -> dont clear token, return error and let it be handled, user should get fitting error message


        // wait for first real token data
        val definitiveRefreshTokenState = refreshTokenFlow.first {
            it != TokenState.Loading
        }

        return when (definitiveRefreshTokenState) {
            TokenState.Loading -> {
                Log.d(TAG, "refreshAccessToken: Token still loading after wait -> clear all tokens.")
                clearAllTokens()
                Result.failure(DataException.Auth.MissingOrInvalidRefreshToken)
            }
            TokenState.NoToken -> {
                Log.d(TAG, "refreshAccessToken: failed because of missing refresh token -> clear all tokens")
                clearAllTokens()
                Result.failure(DataException.Auth.MissingOrInvalidRefreshToken)
            }
            is TokenState.HasToken -> {
                val refreshToken = definitiveRefreshTokenState.token

                Log.d(TAG, "refreshAccessToken: found saved refresh token -> get fresh access token with it")
                val result = this.accountApi.refreshToken(
                    client = "Basic " + Base64.encode("$clientId:$clientSecret".encodeToByteArray()),
                    refreshToken = refreshToken,
                )

                result.fold(
                    onSuccess = { tokenRequestResponse ->
                        Log.d(TAG, "got fresh access token -> save it")
                        saveAccessToken(tokenRequestResponse.accessToken)
                        if (tokenRequestResponse.refreshToken != "") {
                            saveRefreshToken(tokenRequestResponse.refreshToken)
                        }
                        Result.success(tokenRequestResponse.accessToken)
                    },
                    onFailure = { e ->
                        // TODO two cases:
                        //  failed because refresh token is invalid -> clearAllToken (logout)
                        //  failed because of other reason (no connection...) -> dont log out, somehow pass error to HttpClient to return it and handle in ViewModel
                        return when (e) {
                            // do not clear tokens, problem could be temporarily
                            is IOException, is ServerResponseException -> {
                                Log.e(
                                    TAG,
                                    "refreshAccessToken: getting fresh access token failed. problem could be temporarily, not logging out",
                                    e
                                )
                                Result.failure(e)
                            }
                            // clear tokens
                            else -> {
                                Log.e(
                                    TAG,
                                    "refreshAccessToken: getting fresh access token failed. Logging out",
                                    e
                                )
                                clearAllTokens()
                                Result.failure(e)
                            }
                        }
                    }
                )
            }
        }
    }

    private suspend fun saveAccessToken(token: String) {
        dataStore.edit { preferences ->
            preferences[DatastoreKeys.accessToken] = token
        }
    }

    private suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[DatastoreKeys.refreshToken] = token
        }
    }

    override suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.remove(DatastoreKeys.accessToken)
            preferences.remove(DatastoreKeys.refreshToken)
        }
    }
}