package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.SwipeDirection
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.component.rememberSwipeCardState
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackListScreen(
    modifier: Modifier = Modifier,
    trackListViewModel: TrackListViewModel = koinViewModel()
) {
    val TAG = "TrackListScreen"

    val swipeableTracks by trackListViewModel.swipeTracks.collectAsState(
        emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Keine SOngs Ürbig wechsel zu liste")

            swipeableTracks.reversed().forEach {
                key(it.id) {
                    SwipeCard(
                        swipeCardState = rememberSwipeCardState(),
                        onSwiped = { dir ->
                            when (dir) {
                                SwipeDirection.Left -> trackListViewModel.swipeLeft(it)
                                SwipeDirection.Right -> trackListViewModel.swipeRight(it)
                                SwipeDirection.Up -> trackListViewModel.swipeUp(it)
                                SwipeDirection.Down -> {}
                            }
                        },
                    ) {
                        TrackCard(it,
                            modifier = Modifier.size(400.dp))
                    }
                }
            }
        }
    }

//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(trackList) { track ->
//                TrackItem(track, onChangeVote = { newVote ->
//                    trackListViewModel.onChangeVote(track, newVote)
//                })
//            }
//        }

}


@Preview(showBackground = true)
@Composable
private fun TrackListScreenPreview() {
    // Use Theme here
    TrackListScreen()
}