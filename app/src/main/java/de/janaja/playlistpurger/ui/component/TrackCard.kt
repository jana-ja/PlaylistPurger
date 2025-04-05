package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(

                model = track.album.images.firstOrNull()?.url,
                contentDescription = "Cover of Track",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .aspectRatio(1f)
            )
            Text(
                track.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                track.artists.joinToString(", ") { it.name },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackCardPreview() {
    // Use Theme here
    TrackCard(PreviewData.previewTrack)
}