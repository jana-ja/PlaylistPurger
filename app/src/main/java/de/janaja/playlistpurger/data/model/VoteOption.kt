package de.janaja.playlistpurger.data.model

import androidx.compose.ui.graphics.Color
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.ui.component.SwipeDirection

enum class VoteOption(val value: Int, val imgResId: Int, val contentDescription: String, val color: Color) {
    KEEP(1, imgResId = R.drawable.baseline_check_24, contentDescription = "vote keep track", Color.Green),
    DONT_CARE(0, imgResId = R.drawable.outline_circle_24, contentDescription = "vote dont care", Color.Blue),
    REMOVE(-1, imgResId = R.drawable.baseline_close_24, contentDescription = "vote remove song", Color.Red);

    fun getSwipeDirection(): SwipeDirection {
        when (this) {
            KEEP -> return SwipeDirection.Right
            DONT_CARE -> return SwipeDirection.Up
            REMOVE -> return SwipeDirection.Left
        }
    }

    companion object {
        fun fromSwipeDirection(dir: SwipeDirection): VoteOption {
            return when (dir) {
                SwipeDirection.Left -> REMOVE
                SwipeDirection.Right -> KEEP
                SwipeDirection.Up -> DONT_CARE
                SwipeDirection.Down -> DONT_CARE // TODO
            }
        }
    }
}