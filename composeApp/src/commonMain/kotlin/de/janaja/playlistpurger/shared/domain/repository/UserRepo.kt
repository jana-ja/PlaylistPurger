package de.janaja.playlistpurger.shared.domain.repository

import de.janaja.playlistpurger.shared.domain.model.UserWithName

interface UserRepo {
    suspend fun getUserForId(userId: String): Result<UserWithName>
}