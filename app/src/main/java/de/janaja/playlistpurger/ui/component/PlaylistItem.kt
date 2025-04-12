package de.janaja.playlistpurger.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.domain.PreviewData
import de.janaja.playlistpurger.domain.model.Playlist
import de.janaja.playlistpurger.domain.model.User

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
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Image
            AsyncImage(
                model = playlist.imageUrl,
                "Playlist Image",
                modifier = Modifier.fillMaxWidth(0.38f).aspectRatio(1f),
                error = painterResource(R.drawable.img)

            )

            Column(
                Modifier.fillMaxHeight().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name and owner
                Column(
                    Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        playlist.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        playlist.owner.name,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis

                    )
                }

                Row(
                    Modifier.align(Alignment.CenterHorizontally),
                ) {
                    BigCardButton(
                        onClick = {
                            onNavToResult(playlist.id, playlist.name)
                        },
                        icon = R.drawable.baseline_poll_24,
                        text = "Result"
                    )

                    Spacer(Modifier.width(16.dp))

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
}

@Preview(showBackground = true)
@Composable
private fun PlaylistItemPreview() {
    // Use Theme here
    PlaylistItem(PreviewData.previewPlaylist, { _, _ -> }, { _, _ -> })
}
@Preview(showBackground = true)
@Composable
private fun PlaylistItemPreview2() {
    // Use Theme here
    PlaylistItem(PreviewData.previewPlaylist.copy(name = "Langer Playlist Titel der sehr lang ist"), { _, _ -> }, { _, _ -> })
}
@Preview(showBackground = true)
@Composable
private fun PlaylistItemPreview3() {
    // Use Theme here
    PlaylistItem(PreviewData.previewPlaylist.copy(name = "Langer Playlist Titel der sehr lang ist", owner = User(id = "", name = "Langer User name der sehr lang ist")), { _, _ -> }, { _, _ -> })
}

@Preview(showBackground = true, widthDp = 500, heightDp = 200)
@Composable
private fun PlaylistItemPreview4() {
    // Use Theme here
    PlaylistItem(PreviewData.previewPlaylist.copy(name = "Langer Playlist Titel der sehr lang ist", owner = User(id = "", name = "Langer User name der sehr lang ist")), { _, _ -> }, { _, _ -> })
}