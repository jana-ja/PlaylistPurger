package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.janaja.playlistpurger.ui.component.PlaylistItem
import de.janaja.playlistpurger.ui.viewmodel.PlaylistOverviewViewModel

@Composable
fun PlaylistOverviewScreen(
    modifier: Modifier = Modifier,
    welcomeViewModel: PlaylistOverviewViewModel = viewModel()
) {
    val playlists by welcomeViewModel.playlists.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {


        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(playlists) {
                PlaylistItem(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistOverviewScreenPreview() {
    // Use Theme here
    PlaylistOverviewScreen()
}