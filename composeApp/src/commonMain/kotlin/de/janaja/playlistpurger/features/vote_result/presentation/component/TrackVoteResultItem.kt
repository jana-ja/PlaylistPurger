package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.shared.domain.model.Track
import de.janaja.playlistpurger.shared.domain.model.Vote
import de.janaja.playlistpurger.shared.domain.model.VoteOption
import de.janaja.playlistpurger.features.track_voting.presentation.component.VoteButton


@Composable
fun TrackVoteResultItem(
    track: Track,
    voteList: List<Vote>,
    modifier: Modifier = Modifier
) {

    val votesByOption = remember { voteList.groupBy { it.voteOption } }
    val max = remember { votesByOption.values.maxOfOrNull { it.count() } }

    ElevatedCard(modifier = modifier) {
        Column(
                modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {

//                Box(Modifier.fillMaxHeight().aspectRatio(1f).background(Color.Cyan))
//                Box(Modifier.fillMaxHeight().aspectRatio(1f)) {
                    AsyncImage(
                        model = track.album.imageUrl,
                        contentDescription = "album cover",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxHeight().aspectRatio(1f) // doesnt work -> "images have strong opinions about their own size"
                    )
//                }

                Column(
                    Modifier.weight(1f),
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