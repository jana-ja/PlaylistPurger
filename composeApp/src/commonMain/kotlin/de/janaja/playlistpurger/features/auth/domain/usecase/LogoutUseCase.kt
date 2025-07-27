package de.janaja.playlistpurger.features.auth.domain.usecase

import de.janaja.playlistpurger.features.auth.domain.service.AuthService

class LogoutUseCase(
    private val authService: AuthService
) {
    suspend operator fun invoke() {
        authService.logout()
    }
}