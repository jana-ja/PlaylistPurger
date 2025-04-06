package de.janaja.playlistpurger.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.PreviewData

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onNavToVote: (String, String) -> Unit,
    onNavToResult: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column (
                modifier = Modifier
//                    .fillMaxWidth()
                    .weight(1f)
                    .height(IntrinsicSize.Min),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Image
                val imageUrl = playlist.images.firstOrNull()?.url
                AsyncImage(
                    model = imageUrl,
                    "Playlist Image",
                    modifier = Modifier.size(120.dp),
                    error = painterResource(R.drawable.round_rectangle_24)

                )

                // Name and owner
                Column(Modifier.weight(1f)) {
                    Text(playlist.name,
                        style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(playlist.owner.displayName,
                        style = MaterialTheme.typography.bodyMedium)
                }

//            // private or not
//            Icon(
//                painterResource(if (playlist.public) R.drawable.outline_lock_open_24 else R.drawable.outline_lock_24),
//                if (playlist.public) "public playlist" else "private playlist"
//            )
//
//            // collabortaive or not
//            Icon(
//                painterResource(if (playlist.collaborative) R.drawable.baseline_groups_24 else R.drawable.baseline_person_24),
//                if (playlist.collaborative) "collaborative playlist" else "single playlist"
//            )
            }

            Column (
                Modifier,
//                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BigCardButton(
                    onClick = {
                        onNavToResult(playlist.id, playlist.name)
                    },
                    icon = R.drawable.baseline_poll_24,
                    text = "Result"
                )
                BigCardButton(
                    onClick = {
                        onNavToVote(playlist.id, playlist.name)
                    },
                    icon = R.drawable.baseline_compare_arrows_24,
                    text = "Vote"
                )
            }

        }


    }

}

@Composable
private fun BigCardButton(
    onClick: () -> Unit,
    @DrawableRes
    icon: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier
            .padding(8.dp)
            .size(70.dp)
            .clickable {
                onClick()

            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                painterResource(icon),
                null
            )
            Spacer(Modifier.height(4.dp))
            Text(text,
                style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistItemPreview() {
    // Use Theme here
    PlaylistItem(PreviewData.previewPlaylist, { _, _ -> }, { _, _ -> })
}