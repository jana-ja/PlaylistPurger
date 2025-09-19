package de.janaja.playlistpurger.core.ui.util

import androidx.lifecycle.ViewModel
import de.janaja.playlistpurger.core.domain.exception.DataException
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.util.Log
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.generic_error_message
import playlistpurger.composeapp.generated.resources.invalid_access_token
import playlistpurger.composeapp.generated.resources.missing_access_token
import playlistpurger.composeapp.generated.resources.missing_current_user
import playlistpurger.composeapp.generated.resources.missing_refresh_token
import playlistpurger.composeapp.generated.resources.no_internet_error_message
import playlistpurger.composeapp.generated.resources.serialization_error
import playlistpurger.composeapp.generated.resources.server_side_error_message
import playlistpurger.composeapp.generated.resources.token_not_ready
import playlistpurger.composeapp.generated.resources.too_many_requests_error_message

fun DataException.toStringResId(): UiText {
    val stringRes = when(this) {
        DataException.Auth.MissingAccessToken -> Res.string.missing_access_token
        DataException.Auth.MissingCurrentUser -> Res.string.missing_current_user
        DataException.Auth.MissingOrInvalidRefreshToken -> Res.string.missing_refresh_token
        DataException.Auth.TokenNotReady -> Res.string.token_not_ready
        DataException.Remote.InvalidAccessToken -> Res.string.invalid_access_token
        DataException.Remote.NoInternet -> Res.string.no_internet_error_message
        DataException.Remote.RequestTimeout -> Res.string.no_internet_error_message
        DataException.Remote.Serialization -> Res.string.serialization_error
        DataException.Remote.Server -> Res.string.server_side_error_message
        DataException.Remote.TooManyRequests -> Res.string.too_many_requests_error_message
        DataException.Remote.Unknown -> Res.string.generic_error_message
    }

    return UiText.StringResourceId(stringRes)
}

fun ViewModel.handleDataException(
    e: Throwable,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
    onUpdateErrorMessage: (UiText) -> Unit
) {
    if (e is DataException.Remote) {
        if (e is DataException.Remote.InvalidAccessToken)
            onRefresh()
        else {
            onUpdateErrorMessage(e.toStringResId())
        }
    }
    if (e is DataException.Auth) {
        onUpdateErrorMessage(e.toStringResId())
        onLogout()
    }
}

fun <T, R> ViewModel.resultToDataState( // an result extenden
    apiResult: Result<T>,
    successTransform: (T) -> R,
    onRefresh: () -> Unit,
    onLogout: () -> Unit,
): DataState<R> {
    apiResult.fold(
        onSuccess = { allPlaylists ->
            return DataState.Ready(successTransform(allPlaylists))
        },
        onFailure = { e ->
            // look for authorization error, refresh token, retry
            if (e is DataException.Remote) {
                if (e is DataException.Remote.InvalidAccessToken) {
//                    viewModelScope.launch {
                    Log.i("ViewModel", "resultToDataStore: access token is invalid\ntry to refresh...")

                    onRefresh()
//                    }
                    return DataState.Loading
                } else {
                    return DataState.Error(e.toStringResId())
                }
            } else if (e is DataException.Auth) {
//                viewModelScope.launch {
                    onLogout()
//                }
                return DataState.Error(e.toStringResId())

            } else {
                return DataState.Error(UiText.StringResourceId(Res.string.generic_error_message))
            }
        }
    )
}