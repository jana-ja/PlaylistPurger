package de.janaja.playlistpurger.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.R


@Composable
fun VoteButton(
    selected: Boolean,
    onClick: () -> Unit,
    iconResId: Int,
    selectionColor: Color,
    selectionScaling: Float,
    contentDescription: String,
    modifier: Modifier = Modifier.size(32.dp)
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) selectionColor else LocalContentColor.current,
        label = "backgroundColor"
    )

    val scaling by animateFloatAsState(
        targetValue = if (selected) selectionScaling else 1.0f
    )

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(iconResId), contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .scale(scaling),
            tint = backgroundColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VoteButtonSelectedPreview() {
    // Use Theme here
    VoteButton(true, {}, R.drawable.baseline_close_24, Color.Red, 1.5f, "")
}

@Preview(showBackground = true)
@Composable
private fun VoteButtonUnselectedPreview() {
    // Use Theme here
    VoteButton(false, {}, R.drawable.baseline_close_24, Color.Red, 1.5f, "")
}