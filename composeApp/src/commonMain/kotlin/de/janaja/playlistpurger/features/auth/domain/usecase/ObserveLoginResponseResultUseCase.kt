package de.janaja.playlistpurger.features.auth.domain.usecase

import de.janaja.playlistpurger.features.auth.domain.helper.OAuthResponseHelper
import de.janaja.playlistpurger.features.auth.domain.model.LoginResult
import kotlinx.coroutines.flow.Flow

class ObserveLoginResponseResultUseCase(
    private val oAuthResponseHelper: OAuthResponseHelper
) {
    operator fun invoke(): Flow<LoginResult> {
        return oAuthResponseHelper.loginResult
    }
}