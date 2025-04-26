package de.janaja.playlistpurger.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import de.janaja.playlistpurger.util.Log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SwipeCard(
    onSwiped: (SwipeDirection) -> Unit,
    swipeCardState: SwipeCardState = rememberSwipeCardState(),
    content: @Composable () -> Unit
) {
    val offset by remember(swipeCardState) {
        derivedStateOf { swipeCardState.offset }
    }
//    val currentDir by remember(swipeCardState) {
//        derivedStateOf { swipeCardState.currentSwipeDirection }
//    }
    val finalSwipeDirection by remember(swipeCardState) {
        derivedStateOf { swipeCardState.finalSwipeDirection }
    }

//    var buttonClickSwipe by remember { mutableStateOf<SwipeDirection?>(null) }

//    LaunchedEffect(buttonClickSwipe) {
//        buttonClickSwipe?.let {
//            swipeCardState.swipe(it)
//            buttonClickSwipe = null
//        }
//    }

    LaunchedEffect(finalSwipeDirection) {
        Log.d("SwipeCard", "SwipeCard: $finalSwipeDirection")
        finalSwipeDirection?.let(onSwiped)
    }

    Column {
        Box(modifier = Modifier
            .pointerInput(Unit) {
                coroutineScope {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            launch {
                                val original = swipeCardState.offset.targetValue
                                val summed = original + dragAmount
                                val newValue = Offset(
                                    x = summed.x.coerceIn(
                                        -swipeCardState.maxWidth,
                                        swipeCardState.maxWidth
                                    ),
                                    y = summed.y.coerceIn(
                                        -swipeCardState.maxHeight,
                                        swipeCardState.maxHeight
                                    )
                                )
                                if (change.positionChange() != Offset.Zero) change.consume()

                                swipeCardState.drag(newValue)
                            }
                        },
                        onDragEnd = {
                            launch {
                                val coercedOffset = swipeCardState.offset.targetValue
                                    .coerceIn(
                                        listOf(SwipeDirection.Down),
                                        maxHeight = swipeCardState.maxHeight,
                                        maxWidth = swipeCardState.maxWidth
                                    )

                                swipeCardState.dragEnd(coercedOffset)
                            }
                        }
                    )
                }
            }
            .graphicsLayer(
                translationX = offset.value.x,
                translationY = offset.value.y,
                rotationZ = animateFloatAsState(offset.value.x / 50).value
            )) {
            content()
        }

    }
}

private fun Offset.coerceIn(
    blockedDirections: List<SwipeDirection>,
    maxHeight: Float,
    maxWidth: Float,
): Offset {
    return copy(
        x = x.coerceIn(
            if (blockedDirections.contains(SwipeDirection.Left)) {
                0f
            } else {
                -maxWidth
            },
            if (blockedDirections.contains(SwipeDirection.Right)) {
                0f
            } else {
                maxWidth
            }
        ),
        y = y.coerceIn(
            if (blockedDirections.contains(SwipeDirection.Up)) {
                0f
            } else {
                -maxHeight
            },
            if (blockedDirections.contains(SwipeDirection.Down)) {
                0f
            } else {
                maxHeight
            }
        )
    )
}

