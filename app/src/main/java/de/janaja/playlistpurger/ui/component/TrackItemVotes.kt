package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.domain.PreviewData
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.model.Vote
import de.janaja.playlistpurger.domain.model.VoteOption


@Composable
fun TrackItemVotes(
    track: Track,
    voteList: List<Vote>,
    modifier: Modifier = Modifier
) {

    val votesByOption = remember { voteList.groupBy { it.voteOption } }
    val max = remember { votesByOption.values.maxOfOrNull { it.count() } }

    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {

                Text(track.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    track.artists.joinToString(", ") { it.name },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row {
                VoteOption.entries.forEach { vote ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        VoteButton(
                            selected = (((votesByOption[vote]?.count() ?: -1) == max)),
                            onClick = { },
                            iconResId = vote.imgResId,
                            selectionColor = vote.color,
                            selectionScaling = 1.1f,
                            contentDescription = vote.contentDescription,
                            // TODO disable
                        )
                        Text(
                            voteList.count { it.voteOption == vote }.toString(),
                            fontWeight = if (vote == track.vote) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackItemVotesPreview() {
    // Use Theme here
    TrackItemVotes(PreviewData.previewTrack, listOf())
}