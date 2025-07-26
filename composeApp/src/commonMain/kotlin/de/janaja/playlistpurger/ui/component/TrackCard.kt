package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.domain.model.Track

@Composable
fun TrackCard(
    track: Track,
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = track.album.imageUrl,
                contentDescription = "Cover of Track",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                track.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
            Text(
                track.artists.joinToString(", ") { it.name },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }
    }
}
