package de.janaja.playlistpurger.features.auth.data.service

import de.janaja.playlistpurger.features.auth.data.remote.SpotifyClientCredentials
import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.repo.TokenRepo
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import de.janaja.playlistpurger.features.playlist_overview.domain.model.TokenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.io.encoding.ExperimentalEncodingApi

// manages session state
class SpotifyAuthService(
    private val tokenRepo: TokenRepo,
    private val externalScope: CoroutineScope
) : AuthService {
    companion object {
        private const val TAG = "SpotifyAuthService"
    }

    private val spotifyClientCredentials = SpotifyClientCredentials

    override val userLoginState = tokenRepo.accessTokenFlow
        .map { checkToken(it) }
        .stateIn(
            scope = externalScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserLoginState.Loading
        )

    private fun checkToken(tokenState: TokenState): UserLoginState {
        return when (tokenState) {
            is TokenState.HasToken -> UserLoginState.LoggedIn
            TokenState.Loading -> UserLoginState.Loading
            TokenState.NoToken -> UserLoginState.LoggedOut
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun loginWithCode(code: String): Result<Unit> {
        return tokenRepo.exchangeAuthCodeForTokens(code)
    }

    override fun getAuthenticationUrl(): String {
        val urlString = "https://accounts.spotify.com/de/authorize?" +
                "scope=playlist-read-private user-modify-playback-state" +
                "&response_type=code" +
                "&redirect_uri=${spotifyClientCredentials.redirectUri}" + // asdf%3A%2F%2Fcallback
                "&client_id=${spotifyClientCredentials.clientId}" +
                "&show_dialog=true"
        return urlString
    }

    override suspend fun logout() {
        tokenRepo.clearAllTokens()
    }


}