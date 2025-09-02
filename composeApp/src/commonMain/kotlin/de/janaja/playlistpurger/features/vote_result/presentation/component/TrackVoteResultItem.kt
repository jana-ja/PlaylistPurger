package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.core.ui.component.CircleUserImage
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.VoteResult


@Composable
fun TrackVoteResultItem(
    track: Track,
    voteResult: VoteResult,
    modifier: Modifier = Modifier
) {

    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val usersByVoteOption = voteResult.votes.groupBy { it.voteOption }
        .mapValues { it.value.map { it.user } }

    var textColumnSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current

    val backgroundColor = voteResult.resultOption?.color?.makePastelByMixing()
        ?: CardDefaults.elevatedCardColors().containerColor

    ElevatedCard(
        modifier = modifier,
//        colors = CardDefaults.elevatedCardColors().copy(
//            containerColor = backgroundColor
//        ),
        onClick = {
            isExpanded = !isExpanded
        }
    ) {

//        Box(
//            Modifier.fillMaxWidth().height(IntrinsicSize.Min)
//        ) {

//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .width(16.dp)
//                    .fillMaxHeight()
//                    .background(backgroundColor)
//            )

        Column(
            modifier = Modifier.padding(16.dp),//.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {

                // Bypassing Complex Intrinsic Logic: We are not relying as heavily on the nuances of how IntrinsicSize interacts with AsyncImage's own measurement logic for downscaling.
                // multiple recomposition passes that onSizeChanged might introduce (though for a stable list item, it's usually fine).
                if (textColumnSize.height > 0) {
                    // TODO placeholder
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
                        maxLines = 1
                    )
                    Text(
                        track.artists.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1
                    )
                }

                if (textColumnSize.height > 0) {
                    CircleUserImage(
                        track.addedBy,
                        modifier = Modifier
                            .size(
                                width = with(density) { (textColumnSize.height/1.5f).toDp() },
                                height = with(density) { (textColumnSize.height/1.5f).toDp() }
                            )
                    )
                } else {
                    // Placeholder
                    Box(modifier = Modifier.size(1.dp))
                }
            }

            AnimatedVisibility(isExpanded) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Spacer(Modifier.size(0.dp))

                    HorizontalDivider()

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        VotesByUsers(
                            usersByVoteOption = usersByVoteOption,
                            modifier = Modifier.weight(1f)
                        )

//                // TODO show max result?
                        VoteOptionIcon(voteResult.resultOption, Modifier.padding(end = 16.dp))

                    }
                }
            }
        }


//        }

    }
}

fun Color.makePastelByMixing(whiteMixRatio: Float = 0.7f): Color {
    // whiteMixRatio = 0.0 means original color, 1.0 means fully white
    val a = this.alpha
    val r = this.red * (1 - whiteMixRatio) + 1.0f * whiteMixRatio
    val g = this.green * (1 - whiteMixRatio) + 1.0f * whiteMixRatio
    val b = this.blue * (1 - whiteMixRatio) + 1.0f * whiteMixRatio
    return Color(
        red = r.coerceIn(0f, 1f),
        green = g.coerceIn(0f, 1f),
        blue = b.coerceIn(0f, 1f),
        alpha = a
    )
}