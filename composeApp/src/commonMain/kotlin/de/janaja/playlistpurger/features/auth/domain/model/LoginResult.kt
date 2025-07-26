package de.janaja.playlistpurger.features.auth.domain.model

sealed class LoginResult {
    data class SuccessLoginResult(val code: String): LoginResult()
    data class ErrorLoginResult(val errorMessage: String): LoginResult()
}