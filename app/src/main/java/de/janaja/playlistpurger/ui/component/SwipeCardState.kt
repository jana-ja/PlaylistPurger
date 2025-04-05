package de.janaja.playlistpurger.ui.component

import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun rememberSwipeCardState(): SwipeCardState {
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val screenHeight = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
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

    /**
     * The [SwipeDirection] the card was swiped at.
     *
     * Null value means the card has not been swiped fully yet.
     */
    var swipedDirection: SwipeDirection? by mutableStateOf(null)
        private set

    var currentSwipeDirection: SwipeDirection? by mutableStateOf(null)
        private set

    internal suspend fun reset() {
        offset.animateTo(offset(0f, 0f), tween(400))
    }

    suspend fun swipe(direction: SwipeDirection, animationSpec: AnimationSpec<Offset> = tween(400)) {
        val endX = maxWidth * 1.5f
        val endY = maxHeight
        when (direction) {
            SwipeDirection.Left -> offset.animateTo(offset(x = -endX), animationSpec)
            SwipeDirection.Right -> offset.animateTo(offset(x = endX), animationSpec)
            SwipeDirection.Up -> offset.animateTo(offset(y = -endY), animationSpec)
            SwipeDirection.Down -> offset.animateTo(offset(y = endY), animationSpec)
        }
        this.swipedDirection = direction
    }

    private fun offset(x: Float = offset.value.x, y: Float = offset.value.y): Offset {
        return Offset(x, y)
    }

    internal suspend fun drag(direction: SwipeDirection?, x: Float, y: Float) {
        this.currentSwipeDirection = direction
        offset.animateTo(offset(x, y))
    }
}
