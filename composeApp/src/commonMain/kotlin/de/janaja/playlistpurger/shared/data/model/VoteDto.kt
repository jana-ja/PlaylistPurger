package de.janaja.playlistpurger.shared.data.model

import de.janaja.playlistpurger.shared.domain.model.VoteOption
import kotlinx.serialization.Serializable

@Serializable
data class VoteDto(
    val playlistId: String,
    val trackId: String,
    val userId: String,
    val voteOption: VoteOption
)