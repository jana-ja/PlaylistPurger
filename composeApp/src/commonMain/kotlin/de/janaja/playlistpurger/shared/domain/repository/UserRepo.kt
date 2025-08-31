package de.janaja.playlistpurger.shared.domain.repository

import de.janaja.playlistpurger.shared.domain.model.UserDetails
import kotlinx.coroutines.flow.StateFlow

interface UserRepo {
    val currentUser: StateFlow<UserDetails.Full?>
    suspend fun getUserForId(userId: String): Result<UserDetails.Full>
}