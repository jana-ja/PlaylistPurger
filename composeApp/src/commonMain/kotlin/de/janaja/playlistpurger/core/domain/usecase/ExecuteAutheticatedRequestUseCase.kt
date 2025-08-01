package de.janaja.playlistpurger.core.domain.usecase

import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.util.Log
import de.janaja.playlistpurger.features.auth.domain.usecase.LogoutUseCase
import de.janaja.playlistpurger.features.auth.domain.usecase.RefreshTokenUseCase

// TODO richtig einsortieren
class ExecuteAuthenticatedRequestUseCase(
    private val refreshTokenUseCase: RefreshTokenUseCase,
    private val logoutUseCase: LogoutUseCase
) {
    private val TAG = "ExecuteAuthenticatedRequestUseCase"
    suspend operator fun <T> invoke(request: suspend () -> Result<T>): Result<T> {
        Log.d("ExecuteAuthenticatedRequestUseCase", "invoke")
        val result = request()
        result.fold(
            onSuccess = {
                return Result.success(it)
            },
            onFailure = { e ->
                when (e) {
                    is DataException.Remote.InvalidAccessToken -> {

                        Log.i(TAG, "access token is invalid\ntry to refresh...")

                        refreshTokenUseCase().fold(
                            onSuccess = {
                                // try again after refresh
                                return request()
                            },
                            onFailure = {
                                // TODO logout here?
                                //  is logout handled automatically by checkToken in authService? should it really?
                                //  handle logout from viewmodel?
                                return Result.failure(it)
                            }
                        )
                    }
                    is DataException.Auth -> {
                        logoutUseCase()
                        return Result.failure(e)

                    }
                    else -> {
                        // TODO
                        return Result.failure(DataException.Remote.Unknown)
                    }
                }
            }
        )
    }
}