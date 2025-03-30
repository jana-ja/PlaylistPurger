package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.data.remote.PreviewData



@Composable
fun TrackItem(
    track: Track,
    modifier: Modifier = Modifier
) {
    val radioOptions = VoteOption.entries
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }

    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(Modifier.weight(1f)) {

                Text(track.name)
                Text("${track.durationMillis / 1000}s")
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
            Row(modifier.selectableGroup()) {
                radioOptions.forEach { vote ->
                    VoteButton(
                        selected = (vote == selectedOption),
                        onClick = { selectedOption = vote },
                        iconResId = vote.imgResId,
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
    TrackItem(PreviewData.track)
}