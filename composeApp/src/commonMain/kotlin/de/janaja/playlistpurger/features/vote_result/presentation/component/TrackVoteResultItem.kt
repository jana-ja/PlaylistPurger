package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.Vote


@Composable
fun TrackVoteResultItem(
    track: Track,
    voteList: List<Vote>,
    modifier: Modifier = Modifier
) {

    val votesByOption = remember { voteList.groupBy { it.voteOption } }
    val max = remember { votesByOption.values.maxOfOrNull { it.count() } }
    var textColumnSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    ElevatedCard(modifier = modifier) {
        Column(
                modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {

                // Bypassing Complex Intrinsic Logic: We are not relying as heavily on the nuances of how IntrinsicSize interacts with AsyncImage's own measurement logic for downscaling.
                // multiple recomposition passes that onSizeChanged might introduce (though for a stable list item, it's usually fine).
                if (textColumnSize.height > 0) {
                    AsyncImage(
                        model = track.album.imageUrl,
                        contentDescription = "album cover",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(
                                width = with(density) { textColumnSize.height.toDp() },
                                height = with(density) { textColumnSize.height.toDp() }
                            )
                    )
                } else {
                    // Placeholder
                    Box(modifier = Modifier.size(1.dp))
                }

                Column(
                    Modifier.weight(1f)
                        .onSizeChanged { textColumnSize = it },
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        track.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1)
                    Text(
                        track.artists.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                }

                // TODO show max result?
            }
            VotesByUsers(
                usersByVoteOption = votesByOption.mapValues { entry ->
                    entry.value.map { it.user }
                }
            )
        }
    }
}