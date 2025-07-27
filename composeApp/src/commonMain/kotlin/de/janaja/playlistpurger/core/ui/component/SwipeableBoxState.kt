package de.janaja.playlistpurger.core.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity // TODO works on ios?
import de.janaja.playlistpurger.core.ui.util.getScreenHeight
import de.janaja.playlistpurger.core.ui.util.getScreenWidth
import kotlin.math.abs

@Composable
fun rememberSwipeCardState(): SwipeCardState {
    val screenWidth =
        with(LocalDensity.current) {
            getScreenWidth().toPx()
        }
    val screenHeight =
        with(LocalDensity.current) {
            getScreenHeight().toPx()
        }
    return remember {
        SwipeCardState(screenWidth, screenHeight)
    }
}

enum class SwipeDirection {
    Left, Right, Up, Down
}

class SwipeCardState(
    internal val maxWidth: Float,
    internal val maxHeight: Float,
) {
    val offset = Animatable(offset(0f, 0f), Offset.VectorConverter)

    var finalSwipeDirection: SwipeDirection? by mutableStateOf(null)
        private set

    var currentSwipeDirection: SwipeDirection? by mutableStateOf(null)
        private set

    internal suspend fun reset() {
        offset.animateTo(offset(0f, 0f), tween(400))
    }

    suspend fun swipe(
        direction: SwipeDirection,
        animationSpec: AnimationSpec<Offset> = tween(400)
    ) {
        this.currentSwipeDirection = direction // for onClick button animation
        val endX = maxWidth * 1.5f
        val endY = maxHeight
        when (direction) {
            SwipeDirection.Left -> offset.animateTo(offset(x = -endX), animationSpec)
            SwipeDirection.Right -> offset.animateTo(offset(x = endX), animationSpec)
            SwipeDirection.Up -> offset.animateTo(offset(y = -endY), animationSpec)
            SwipeDirection.Down -> offset.animateTo(offset(y = endY), animationSpec)
        }
        this.finalSwipeDirection = direction
    }

    private fun offset(x: Float = offset.value.x, y: Float = offset.value.y): Offset {
        return Offset(x, y)
    }

    internal suspend fun drag(newOffset: Offset) {
        this.currentSwipeDirection = getDirection(newOffset)
        offset.animateTo(newOffset)
    }

    internal suspend fun dragEnd(newOffset: Offset) {
        val dir = this.getDirection(
            newOffset
        )
        if (dir == null) {
            this.reset()
        } else {
            this.swipe(dir)
        }
    }

    private fun getDirection(offset: Offset): SwipeDirection? {
        val horizontalTravel = abs(offset.x)
        val verticalTravel = abs(offset.y)
        if (hasNotTravelledEnough(offset)) {
            return null
        }
        return if (horizontalTravel > verticalTravel) {
            if (offset.x > 0)
                SwipeDirection.Right
            else
                SwipeDirection.Left
        } else {
            if (offset.y < 0)
                SwipeDirection.Up
            else
                SwipeDirection.Down
        }
    }

    private fun hasNotTravelledEnough(
        offset: Offset,
    ): Boolean {
        return abs(offset.x) < maxWidth / 4 &&
//            abs(offset.y) < maxHeight / 4 // TODO das macht keinen sinn karte ist viel weniger hoch als bildschirm,
                abs(offset.y) < maxWidth / 4 // TODO richtig lösen
    }
}
