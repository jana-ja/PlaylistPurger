package de.janaja.playlistpurger.ui.component

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import de.janaja.playlistpurger.data.model.Track
import kotlinx.coroutines.delay
import kotlinx.serialization.json.JsonNull.content
import kotlin.math.roundToInt

@Composable
fun SwipeCard(
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    swipeThreshold: Float = 400f,
    sensitivityFactor: Float = 3f,
    content: @Composable () -> Unit
) {
    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    var reset by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d("SwipeCard", "SwipeCard: init")
    }
//    Log.d("SwipeCard", "SwipeCard: recompose")

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(300)
            onSwipeRight.invoke()
            dismissRight = false
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(300)
            onSwipeLeft.invoke()
            dismissLeft = false
        }
    }

    LaunchedEffect(reset) {
        if (reset) {
            offset = 10000f
            delay(100)
            offset = 0f
        }
    }

    Box(modifier = Modifier
        .offset { IntOffset(offset.roundToInt(), 0) }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    offset += (dragAmount / density) * sensitivityFactor
                    if (change.positionChange() != Offset.Zero) change.consume()
                },
                onDragEnd = {
                    Log.d("TrackList", "onDragEnd: $offset")
                    when {
                        offset > swipeThreshold -> {
                            dismissRight = true
                            Log.d("TrackList", "SwipeCard: dismiss right")
                        }

                        offset < -swipeThreshold -> {
                            dismissLeft = true
                            Log.d("TrackList", "SwipeCard: dismiss left")
                        }

                    }

                    reset = true
                }
            )
        }
        .graphicsLayer(
            alpha = 10f - animateFloatAsState(if (dismissRight) 1f else 0f).value,
            rotationZ = animateFloatAsState(offset / 50).value
        )) {
        content()
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun SwipeUiPreview() {
//    // Use Theme here
//    SwipeCard()
//}