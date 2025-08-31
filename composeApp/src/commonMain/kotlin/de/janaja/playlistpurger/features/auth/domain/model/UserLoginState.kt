package de.janaja.playlistpurger.features.auth.domain.model

sealed class UserLoginState {
    data object LoggedIn: UserLoginState()
    data object LoggedOut: UserLoginState()
    data object Loading: UserLoginState()
}