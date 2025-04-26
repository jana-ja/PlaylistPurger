package de.janaja.playlistpurger.domain.model

import androidx.compose.ui.graphics.Color
import de.janaja.playlistpurger.ui.component.SwipeDirection
import org.jetbrains.compose.resources.DrawableResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_check_24
import playlistpurger.composeapp.generated.resources.baseline_close_24
import playlistpurger.composeapp.generated.resources.outline_circle_24

enum class VoteOption(val value: Int) {
    KEEP(1),
    DONT_CARE(0),
    REMOVE(-1);

    val resource: DrawableResource
        get() {
            return when (this) {
                KEEP -> Res.drawable.baseline_check_24
                DONT_CARE -> Res.drawable.outline_circle_24
                REMOVE -> Res.drawable.baseline_close_24
            }
        }

    val contentDescription: String
        get() {
            return when (this) {
                KEEP -> "vote keep track"
                DONT_CARE -> "vote dont care"
                REMOVE -> "vote remove song"
            }
        }

    val color: Color
        get() {
            return when (this) {
                KEEP -> Color.Green
                DONT_CARE -> Color.Blue
                REMOVE -> Color.Red
            }
        }

    val swipeDirection: SwipeDirection
        get() {
            return when (this) {
                KEEP -> SwipeDirection.Right
                DONT_CARE -> SwipeDirection.Up
                REMOVE -> SwipeDirection.Left
            }
        }

    companion object {
        fun fromSwipeDirection(dir: SwipeDirection): VoteOption? {
            return when (dir) {
                SwipeDirection.Left -> REMOVE
                SwipeDirection.Right -> KEEP
                SwipeDirection.Up -> DONT_CARE
                SwipeDirection.Down -> null
            }
        }
    }
}