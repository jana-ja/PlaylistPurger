package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import org.jetbrains.compose.resources.painterResource


@Composable
fun TrackVoteResultItem(
    track: Track,
    voteList: List<Vote>,
    modifier: Modifier = Modifier
) {

    val votesByOption = remember { voteList.groupBy { it.voteOption } }
    val max = remember { votesByOption.values.maxOfOrNull { it.count() } }
//    var textColumnSize by remember { mutableStateOf(IntSize.Zero) }
//    val density = LocalDensity.current

    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            track.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1)
                        Text(
                            track.artists.joinToString(", ") { it.name },
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }

                VotesByUsers(
                    usersByVoteOption = votesByOption.mapValues { entry ->
                        entry.value.map { it.user }
                    }
                )
            }


            // TODO show max result?
            Box {
                AsyncImage(
                    model = track.album.imageUrl,
                    contentDescription = "album cover",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .aspectRatio(1f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .aspectRatio(1f)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)),
                                startY = 0f, // Or some fraction of height
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )
                // Your Text or Icon, typically aligned to the bottom or within the gradient
                Icon(
                    painter = painterResource(VoteOption.KEEP.resource),
                    contentDescription = null,
                    modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp),
                    tint = Color.White

                )
            }
        }
    }
}