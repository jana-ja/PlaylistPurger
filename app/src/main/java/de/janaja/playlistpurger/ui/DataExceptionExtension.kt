package de.janaja.playlistpurger.ui

import androidx.lifecycle.ViewModel
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.domain.exception.DataException

fun DataException.toStringResId(): UiText.StringResourceId {
    val stringRes = when(this) {
        DataException.Auth.MissingAccessToken -> R.string.generic_error_message
        DataException.Auth.MissingCurrentUser -> R.string.generic_error_message
        DataException.Auth.MissingOrInvalidRefreshToken -> R.string.generic_error_message
        DataException.Remote.InvalidAccessToken -> R.string.generic_error_message
        DataException.Remote.NoInternet -> R.string.no_internet_error_message
        DataException.Remote.RequestTimeout -> R.string.no_internet_error_message
        DataException.Remote.Serialization -> R.string.generic_error_message
        DataException.Remote.Server -> R.string.server_side_error_message
        DataException.Remote.TooManyRequests -> R.string.too_many_requests_error_message
        DataException.Remote.Unknown -> R.string.generic_error_message
    }

    return UiText.StringResourceId(stringRes)
}
// test
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