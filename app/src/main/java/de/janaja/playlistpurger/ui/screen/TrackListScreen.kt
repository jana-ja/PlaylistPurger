package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.janaja.playlistpurger.ui.component.TrackItem
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel

@Composable
fun TrackListScreen(
    modifier: Modifier = Modifier,
    trackListViewModel: TrackListViewModel = viewModel()
) {

    val trackList by trackListViewModel.trackList.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(trackList) { track ->
                TrackItem(track)
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun TrackListScreenPreview() {
    // Use Theme here
    TrackListScreen()
}