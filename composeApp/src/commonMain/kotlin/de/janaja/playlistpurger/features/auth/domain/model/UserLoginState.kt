package de.janaja.playlistpurger.features.auth.domain.model

import de.janaja.playlistpurger.shared.domain.model.UserWithName

sealed class UserLoginState {
    data class LoggedIn(val user: UserWithName): UserLoginState()
    data object LoggedOut: UserLoginState()
    data object Loading: UserLoginState()
}