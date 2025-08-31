package de.janaja.playlistpurger.features.auth.data.remote

import de.janaja.playlistpurger.hiddenClientSecret

object SpotifyClientCredentials {
    val clientSecret = hiddenClientSecret // TODO switch to kcmp pkce
    val clientId = "1f7401f5d27847b99a6dfe6908c5ccac"
    val redirectUri = "asdf://callback"
}