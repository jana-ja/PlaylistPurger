package de.janaja.playlistpurger.features.auth.domain.model

import de.janaja.playlistpurger.shared.domain.model.UserDetails

sealed class UserLoginState {
    data class LoggedIn(val user: UserDetails.Full): UserLoginState()
    data object LoggedOut: UserLoginState()
    data object Loading: UserLoginState()
}