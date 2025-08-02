package de.janaja.playlistpurger.features.auth.domain.helper

import de.janaja.playlistpurger.features.auth.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow

interface OAuthResponseHelper {
    val loginResult: Flow<LoginResult>
    fun handleResponseUrl(url: String)
}