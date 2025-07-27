package de.janaja.playlistpurger.features.playlist_overview.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.core.ui.component.BigCardButton
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_compare_arrows_24
import playlistpurger.composeapp.generated.resources.baseline_poll_24
import playlistpurger.composeapp.generated.resources.img

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
                error = painterResource(Res.drawable.img)

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
                            onNavToVote(playlist.id, playlist.name)
                        },
                        icon = Res.drawable.baseline_compare_arrows_24,
                        text = "Vote"
                    )

                    Spacer(Modifier.width(16.dp))

                    BigCardButton(
                        onClick = {
                            onNavToResult(playlist.id, playlist.name)
                        },
                        icon = Res.drawable.baseline_poll_24,
                        text = "Result"
                    )
                }
            }
        }
    }
}