package de.janaja.playlistpurger.features.playlist_overview.domain.model

sealed class TokenState {
    object Loading: TokenState()
    data class HasToken(val token: String): TokenState()
    object NoToken: TokenState()
}