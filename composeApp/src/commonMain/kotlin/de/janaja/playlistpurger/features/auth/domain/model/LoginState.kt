package de.janaja.playlistpurger.features.auth.domain.model

import de.janaja.playlistpurger.shared.domain.model.User

sealed class LoginState {
    data class LoggedIn(val user: User): LoginState()
    data object LoggedOut: LoginState()
    data object Loading: LoginState()
}