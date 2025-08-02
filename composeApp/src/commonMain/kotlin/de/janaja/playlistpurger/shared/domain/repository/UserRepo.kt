package de.janaja.playlistpurger.shared.domain.repository

import de.janaja.playlistpurger.shared.domain.model.UserDetails

interface UserRepo {
    suspend fun getUserForId(userId: String): Result<UserDetails.Full>
}