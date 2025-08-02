package de.janaja.playlistpurger.shared.domain.model

import androidx.compose.ui.graphics.Color
import de.janaja.playlistpurger.core.util.ColorGenerator

interface UserWithColor {
    val color: Color
}

sealed class UserDetails : UserIdentifiable {

    data class Minimal(override val id: String) : UserDetails(), UserWithColor {
        override val color: Color by lazy { ColorGenerator.generatePastelColorFromSeed(id) }
    }

    data class Partial(
        override val id: String,
        override val name: String,
    ) : UserDetails(), UserWithName, UserWithColor {
        override val color: Color by lazy { ColorGenerator.generatePastelColorFromSeed(id) }
    }

    data class Full(
        override val id: String,
        override val name: String,
        val thumbnailUrl: String
    ) : UserDetails(), UserWithName
}