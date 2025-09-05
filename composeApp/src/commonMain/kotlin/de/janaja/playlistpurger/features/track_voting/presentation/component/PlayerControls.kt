package de.janaja.playlistpurger.features.track_voting.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.shared.domain.model.Track
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_pause_24
import playlistpurger.composeapp.generated.resources.baseline_play_arrow_24

@Composable
fun PlayerControls(
    track: Track?,
    isPlaying: Boolean,
    onClickPlayPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onClickPlayPause,
            enabled = track != null
        ) {
            val imgRes = if (isPlaying) Res.drawable.baseline_pause_24 else Res.drawable.baseline_play_arrow_24
            val desc = if (isPlaying) "Pause Track ${track?.name}" else "Play Track ${track?.name}"

            Icon(
                painter = painterResource(imgRes),
                contentDescription = desc,
                modifier = Modifier.size(40.dp)
            )
        }

        // TODO add skip 5 or 10 sec button
    }
}