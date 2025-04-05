package de.janaja.playlistpurger.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
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
    contentDescription: String,
    modifier: Modifier = Modifier.size(32.dp)
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) selectionColor else Color.Black,
        label = "backgroundColor"
    )

    val scaling by animateFloatAsState(
        targetValue = if (selected) 1.5f else 1.0f
    )

    Box(
        modifier = modifier
            .clickable(onClick = onClick)
//            .scale(2f)
    ) {
//        if (selected) {

            Icon(
                painter = painterResource(iconResId), contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scaling)
                    ,
                tint = backgroundColor
            )

//        } else {
//
//            Icon(
//                painter = painterResource(iconResId), contentDescription = contentDescription,
//                modifier = Modifier.fillMaxSize()
//            )
//
//        }
    }


}

@Preview(showBackground = true)
@Composable
private fun VoteButtonSelectedPreview() {
    // Use Theme here
    VoteButton(true, {}, R.drawable.baseline_close_24, Color.Red,"")
}

@Preview(showBackground = true)
@Composable
private fun VoteButtonUnselectedPreview() {
    // Use Theme here
    VoteButton(false, {}, R.drawable.baseline_close_24, Color.Red,"")
}