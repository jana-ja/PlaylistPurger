package de.janaja.playlistpurger.core.ui.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil3.compose.AsyncImage

@Composable
fun CircleAsyncImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {

    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier.clip(CircleShape)
    )
}