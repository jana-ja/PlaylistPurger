package de.janaja.playlistpurger.features.auth.domain.usecase

import androidx.compose.ui.platform.UriHandler
import de.janaja.playlistpurger.features.auth.domain.service.AuthService

class InitiateThirdPartyAuthUseCase(
    private val authService: AuthService
) {
    operator fun invoke(uriHandler: UriHandler): Result<Unit> {
        return try {
            uriHandler.openUri(authService.getAuthenticationUrl())
            Result.success(Unit)
        } catch (e: Exception) {
            // TODO custom exception
            Result.failure(e)
        }
    }
}