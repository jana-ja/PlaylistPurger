package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.data.PreviewData
import de.janaja.playlistpurger.data.model.Track

@Composable
fun TrackCard(
    track: Track,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(

                model = track.album.images.firstOrNull()?.url,
                contentDescription = "Cover of Track",
                modifier = Modifier.size(300.dp)
            )
            Text(track.name)
            Text("${track.durationMillis / 1000}s")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackCardPreview() {
    // Use Theme here
    TrackCard(PreviewData.previewTrack)
}