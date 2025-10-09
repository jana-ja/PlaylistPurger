package de.janaja.playlistpurger.features.vote_result.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.features.playlist_overview.domain.model.Playlist
import de.janaja.playlistpurger.shared.domain.model.Track
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_check_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveRemoveSongsDialog(
    onDismiss: () -> Unit,
    onSave: (playlist: Playlist) -> Unit,
    playlists: List<Playlist>,
    modifier: Modifier = Modifier
) {
    /*
    wenn nicht song selected dann disable dings
     */
    var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Rausgevotetete Songs speichern",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                "Playlist auswählen pls",
                style = MaterialTheme.typography.labelLarge
            )

            LazyColumn(
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(playlists) { playlist ->
                    Row(
                        Modifier
                            .clickable {
                                selectedPlaylist = playlist
                            }
                    ) {
                        Text(
                            text = playlist.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 4.dp)
                        )
                        if (selectedPlaylist?.id == playlist.id)
                            Icon(
                                painter = painterResource(Res.drawable.baseline_check_24),
                                contentDescription = "select playlist"
                            )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Abbrechen")
                }
                Spacer(Modifier.width(8.dp))
                TextButton(
                    onClick = { selectedPlaylist?.let {
                        onSave(it)
                        onDismiss()
                    } },
                    enabled = selectedPlaylist != null
                ) {
                    Text("Speichern")
                }
            }
        }
    }

//    AlertDialog(
//        title = {
//            Text(text = "Songs speichern")
//        },
//        text = {
//            Column {
//                Text(text = "Rausgevotetete Songs speichern")
//
//                LazyColumn(
//                    modifier = Modifier.height(250.dp)
//                ) {
//                    items(playlists) { playlist ->
//                        Text(text = playlist.name)
//                    }
//                }
//            }
//        },
//        onDismissRequest = onDismiss,
//        confirmButton = {
//            TextButton(
//                onClick = onSave
//            ) {
//                Text("Speichern")
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = onDismiss
//            ) {
//                Text("Abbrechen")
//            }
//        }
//    )
}