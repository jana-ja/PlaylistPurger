package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.domain.PreviewData
import de.janaja.playlistpurger.domain.model.Track


@Composable
fun TrackItem(
    track: Track,
    onChangeVote: (VoteOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val radioOptions = VoteOption.entries

    ElevatedCard(modifier = modifier) {
//    OutlinedCard(modifier = modifier) {
//    Card(modifier = modifier) {
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

            // Radio Group hatte sehr interessantes
            /*
            .selectable(
                selected = (vote == selectedOption),
                onClick = { onOptionSelected(vote) },
                role = Role.RadioButton
            )
             */


            // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
            Row(modifier.selectableGroup(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                radioOptions.forEach { vote ->
                    VoteButton(
                        selected = (vote == track.vote),
                        onClick = { onChangeVote(vote) },
                        iconResId = vote.imgResId,
                        selectionColor = vote.color,
                        selectionScaling = 1.1f,
                        contentDescription = vote.contentDescription
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackItemPreview() {
    // Use Theme here
    Column(
        Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        TrackItem(PreviewData.previewTrack, {})
        TrackItem(PreviewData.previewTrack, {})
        TrackItem(PreviewData.previewTrack, {})
        TrackItem(PreviewData.previewTrack, {})
        TrackItem(PreviewData.previewTrack, {})
    }
}