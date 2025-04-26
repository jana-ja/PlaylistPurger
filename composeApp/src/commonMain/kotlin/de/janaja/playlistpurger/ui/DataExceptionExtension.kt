package de.janaja.playlistpurger.ui

import androidx.lifecycle.ViewModel
import de.janaja.playlistpurger.domain.exception.DataException
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.generic_error_message
import playlistpurger.composeapp.generated.resources.no_internet_error_message
import playlistpurger.composeapp.generated.resources.server_side_error_message
import playlistpurger.composeapp.generated.resources.too_many_requests_error_message

fun DataException.toStringResId(): UiText {
    val stringRes = when(this) {
        DataException.Auth.MissingAccessToken -> Res.string.generic_error_message
        DataException.Auth.MissingCurrentUser -> Res.string.generic_error_message
        DataException.Auth.MissingOrInvalidRefreshToken -> Res.string.generic_error_message
        DataException.Remote.InvalidAccessToken -> Res.string.generic_error_message
        DataException.Remote.NoInternet -> Res.string.no_internet_error_message
        DataException.Remote.RequestTimeout -> Res.string.no_internet_error_message
        DataException.Remote.Serialization -> Res.string.generic_error_message
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