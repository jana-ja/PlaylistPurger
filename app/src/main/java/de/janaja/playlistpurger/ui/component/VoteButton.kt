package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import de.janaja.playlistpurger.R


@Composable
fun VoteButton(
    selected: Boolean,
    onClick: () -> Unit,
    iconResId: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    if (selected) {
        FilledTonalIconButton(
            onClick = onClick,
            modifier = modifier,
            colors = IconButtonDefaults.filledTonalIconButtonColors().copy(containerColor = IconButtonDefaults.filledTonalIconButtonColors().containerColor.copy(alpha = 0.7f))
        ) {
            Icon(
                painter = painterResource(iconResId), contentDescription = contentDescription,
                )
        }
    } else {
        IconButton(
            onClick = onClick,
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(iconResId), contentDescription = contentDescription,
            )
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun VoteButtonPreview() {
    // Use Theme here
    VoteButton(true, {}, R.drawable.baseline_close_24, "")
}