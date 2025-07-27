package de.janaja.playlistpurger.shared.domain.repository

import de.janaja.playlistpurger.shared.domain.model.User

interface UserRepo {
    suspend fun getUserForId(userId: String): Result<User>
}