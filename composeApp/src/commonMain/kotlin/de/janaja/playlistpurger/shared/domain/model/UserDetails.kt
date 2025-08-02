package de.janaja.playlistpurger.shared.domain.model

import androidx.compose.ui.graphics.Color
import de.janaja.playlistpurger.core.util.ColorGenerator


sealed class UserDetails : UserIdentifiable, UserWithColor {

    data class Minimal(override val id: String) : UserDetails() {
        override val color: Color by lazy { ColorGenerator.generatePastelColorFromSeed(id) }
    }

    data class Partial(
        override val id: String,
        override val name: String,
    ) : UserDetails(), UserWithName {
        override val color: Color by lazy { ColorGenerator.generatePastelColorFromSeed(id) }
    }

    data class Full(
        override val id: String,
        override val name: String,
        val thumbnailUrl: String?
    ) : UserDetails(), UserWithName {
        override val color: Color by lazy { ColorGenerator.generatePastelColorFromSeed(id) }
    }
}