package de.janaja.playlistpurger.features.auth.domain.usecase

import de.janaja.playlistpurger.features.auth.domain.service.AuthService

class LoginWithCodeUseCase(
    private val authService: AuthService,
) {
    suspend operator fun invoke(code: String): Result<Unit> {
        return authService.loginWithCode(code)
    }
}