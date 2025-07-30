package de.janaja.playlistpurger.features.auth.domain.usecase

import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.domain.service.AuthService
import kotlinx.coroutines.flow.Flow

class ObserveUserLoginStateUseCase(
    private val authService: AuthService
) {
    operator fun invoke(): Flow<UserLoginState> {
        return authService.userLoginState
    }
}