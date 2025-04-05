package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.data.model.Playlist
import de.janaja.playlistpurger.data.PreviewData

@Composable
fun PlaylistItem(
    playlist: Playlist,
    onNavToResult: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            val imageUrl = playlist.images.firstOrNull()?.url
            AsyncImage(
                model = imageUrl,
                "Playlist Image",
                modifier = Modifier.size(50.dp)
            )

            // Name and owner
            Column(Modifier.weight(1f)) {
                Text(playlist.name)
                Spacer(Modifier.weight(1f))
                Text(playlist.owner.displayName)
            }

            IconButton(onClick = {
                onNavToResult(playlist.id, playlist.name)
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    "go to vote result"
                )
            }

//            // private or not
//            Icon(
//                painterResource(if (playlist.public) R.drawable.outline_lock_open_24 else R.drawable.outline_lock_24),
//                if (playlist.public) "public playlist" else "private playlist"
//            )
//
//            // collabortaive or not
//            Icon(
//                painterResource(if (playlist.collaborative) R.drawable.baseline_groups_24 else R.drawable.baseline_person_24),
//                if (playlist.collaborative) "collaborative playlist" else "single playlist"
//            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun PlaylistItemPreview() {
    // Use Theme here
    PlaylistItem(PreviewData.previewPlaylist, {_,_ ->})
}