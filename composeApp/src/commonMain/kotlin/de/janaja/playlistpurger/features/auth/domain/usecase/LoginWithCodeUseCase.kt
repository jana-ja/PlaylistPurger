package de.janaja.playlistpurger.features.auth.domain.usecase

import de.janaja.playlistpurger.core.domain.usecase.ExecuteAuthenticatedRequestUseCase
import de.janaja.playlistpurger.features.auth.domain.service.AuthService

class LoginWithCodeUseCase(
    private val authService: AuthService,
    private val executeAuthenticatedRequestUseCase: ExecuteAuthenticatedRequestUseCase
) {
    suspend operator fun invoke(code: String): Result<Unit> {
        return executeAuthenticatedRequestUseCase {
            authService.loginWithCode(code)
        }
    }
}