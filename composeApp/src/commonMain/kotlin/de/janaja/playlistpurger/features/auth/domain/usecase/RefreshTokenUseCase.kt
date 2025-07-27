package de.janaja.playlistpurger.features.auth.domain.usecase

import de.janaja.playlistpurger.features.auth.domain.service.AuthService

// TODO richtig einsortieren
class RefreshTokenUseCase(
    private val authService: AuthService
) {
    suspend operator fun invoke(): Result<Unit> {
        return authService.refreshToken()
    }
}