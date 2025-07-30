package de.janaja.playlistpurger.features.auth.domain.model

import de.janaja.playlistpurger.shared.domain.model.User

sealed class UserLoginState {
    data class LoggedIn(val user: User): UserLoginState()
    data object LoggedOut: UserLoginState()
    data object Loading: UserLoginState()
}