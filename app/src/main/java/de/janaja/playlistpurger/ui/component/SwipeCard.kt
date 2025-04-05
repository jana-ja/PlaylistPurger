package de.janaja.playlistpurger.ui.component

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import de.janaja.playlistpurger.data.model.VoteOption
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonNull.content
import kotlin.math.abs

@Composable
fun SwipeCard(
    onSwiped: (SwipeDirection) -> Unit,
    swipeCardState: SwipeCardState = rememberSwipeCardState(),
    content: @Composable () -> Unit
) {


    val offset2 by remember(swipeCardState) {
        derivedStateOf { swipeCardState.offset }
    }
    val currentDir by remember(swipeCardState) {
        derivedStateOf { swipeCardState.currentSwipeDirection }
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
                                val dir = getDirection(
                                    newValue,
                                    swipeCardState.maxWidth,
                                    swipeCardState.maxHeight
                                )
                                Log.d("GRRR", "SwipeCard: $dir")
                                swipeCardState.drag(dir, newValue.x, newValue.y)
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

                                val dir = getDirection(
                                    coercedOffset,
                                    swipeCardState.maxWidth,
                                    swipeCardState.maxHeight
                                )
                                if (dir == null) {
                                    swipeCardState.reset()
                                } else {
                                    swipeCardState.swipe(dir)
                                    onSwiped(dir)
                                }
                            }
                        }
                    )
                }
            }
            .graphicsLayer(
                translationX = offset2.value.x,
                translationY = offset2.value.y,
//                alpha = 10f - animateFloatAsState(if (dismissRight) 1f else 0f).value,
                rotationZ = animateFloatAsState(offset2.value.x / 50).value
            )) {
            content()
        }

        Row() {
            VoteOption.entries.reversed().forEach { vote ->
                VoteButton(
                    selected = (when (currentDir) {
                        SwipeDirection.Up -> vote == VoteOption.DONT_CARE
                        SwipeDirection.Left -> vote == VoteOption.REMOVE
                        SwipeDirection.Right -> vote == VoteOption.KEEP
                        else -> false

                    }),
                    onClick = {
//                        coroutineScope {
//                            when (vote) {
//                                VoteOption.KEEP -> {
//                                    swipeCardState.swipe(Direction.Right)
////                                dismissRight = true
//                                }
//
//                                VoteOption.DONT_CARE -> {}
//                                VoteOption.REMOVE -> {
//                                    dismissLeft = true
//                                }
//                            }
//                        }
                    },
                    iconResId = vote.imgResId,
                    contentDescription = vote.contentDescription
                )
            }
        }

    }
}

private fun getDirection(offset: Offset, maxWidth: Float, maxHeight: Float): SwipeDirection? {
    val horizontalTravel = abs(offset.x)
    val verticalTravel = abs(offset.y)
    if (hasNotTravelledEnough(maxWidth, maxHeight, offset)) {
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

private fun hasNotTravelledEnough(
    maxWidth: Float,
    maxHeight: Float, //: SwipeCardState,
    offset: Offset,
): Boolean {
    return abs(offset.x) < maxWidth / 4 &&
//            abs(offset.y) < maxHeight / 4 // TODO das macht keinen sinn karte ist viel weniger hoch als bildschirm,
            abs(offset.y) < maxWidth / 4 // TODO richtig lösen
}
