package de.janaja.playlistpurger.domain.model

sealed class LoginState {
    data class LoggedIn(val user: User): LoginState()
    data object LoggedOut: LoginState()
    data object Loading: LoginState()
}