package de.janaja.playlistpurger.ui.component

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.data.model.VoteOption
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
    val currentDir by remember(swipeCardState) {
        derivedStateOf { swipeCardState.currentSwipeDirection }
    }
    val finalSwipeDirection by remember(swipeCardState) {
        derivedStateOf { swipeCardState.finalSwipeDirection }
    }

    var buttonClickSwipe by remember { mutableStateOf<SwipeDirection?>(null) }

    LaunchedEffect(buttonClickSwipe) {
        buttonClickSwipe?.let {
            swipeCardState.swipe(it)
            buttonClickSwipe = null
        }
    }

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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            VoteOption.entries.reversed().forEach { vote ->
                VoteButton(
                    selected = (when (currentDir) {
                        SwipeDirection.Up -> vote == VoteOption.DONT_CARE
                        SwipeDirection.Left -> vote == VoteOption.REMOVE
                        SwipeDirection.Right -> vote == VoteOption.KEEP
                        else -> false

                    }),
                    onClick = {
                        buttonClickSwipe = when (vote) {
                            VoteOption.KEEP -> SwipeDirection.Right
                            VoteOption.DONT_CARE -> SwipeDirection.Up
                            VoteOption.REMOVE -> SwipeDirection.Left
                        }
                    },
                    iconResId = vote.imgResId,
                    contentDescription = vote.contentDescription,
                )
            }
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

