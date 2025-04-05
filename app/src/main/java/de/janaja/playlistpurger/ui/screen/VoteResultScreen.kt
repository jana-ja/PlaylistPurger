package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.ui.component.TrackItem
import de.janaja.playlistpurger.ui.component.TrackItemVotes
import de.janaja.playlistpurger.ui.viewmodel.VoteResultViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VoteResultScreen(
    modifier: Modifier = Modifier,
    voteResultViewModel: VoteResultViewModel = koinViewModel()
) {

    val tracksWithAllVotes by voteResultViewModel.blub.collectAsState()
    // song mit den vote dingern mit anzahl der votes, wenn alle abgestimmt haben ist grün hinterlegt oder so

    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tracksWithAllVotes) { (track, voteList) ->
                TrackItemVotes(track, voteList)
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun VoteResultScreenPreview() {
    // Use Theme here
    VoteResultScreen()
}