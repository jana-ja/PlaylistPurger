package de.janaja.playlistpurger.domain.model

sealed class LoginResult {
    data class SuccessLoginResult(val code: String): LoginResult()
    data class ErrorLoginResult(val errorMessage: String): LoginResult()
}