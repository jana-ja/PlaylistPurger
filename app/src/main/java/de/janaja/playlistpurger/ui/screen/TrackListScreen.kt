package de.janaja.playlistpurger.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.janaja.playlistpurger.data.PreviewData
import de.janaja.playlistpurger.data.PreviewData.track
import de.janaja.playlistpurger.data.model.Track
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.component.TrackItem
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackListScreen(
    modifier: Modifier = Modifier,
    trackListViewModel: TrackListViewModel = koinViewModel()
) {
    val TAG = "TrackListScreen"

    val trackList by trackListViewModel.trackList.collectAsState()

//    val currentTrack by trackListViewModel.currentSwipeTrack.collectAsState(null)
//    val nextTrack by trackListViewModel.nextSwipeTrack.collectAsState(null)

    val swipableTracks by trackListViewModel.swipeTracks.collectAsState(
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
                .height(400.dp)
        ) {
            Text("Keine SOngs Ürbig wechsel zu liste")

            swipableTracks.forEach {
                SwipeCard(
                    onSwipeRight = {
                        Log.d(TAG, "onSwipeRight next: ")
                        trackListViewModel.swipeRight(it)
                    },
                    onSwipeLeft = {
                        Log.d(TAG, "onSwipeLeft next: ")
                        trackListViewModel.swipeLeft(it)
                    }
                ) {
                    TrackCard(it)
                }
            }

//            nextTrack?.let {
//                SwipeCard(
//                    onSwipeRight = {
//                        Log.d(TAG, "onSwipeRight next: ")
//                        trackListViewModel.swipeRight()
//                    },
//                    onSwipeLeft = {
//                        Log.d(TAG, "onSwipeLeft next: ")
//                        trackListViewModel.swipeLeft()
//                    }
//                ) {
//                    TrackCard(it)
//                }
//            }
//
//            currentTrack?.let {
//                SwipeCard(
//                    onSwipeRight = {
//                        Log.d(TAG, "onSwipeRight current: ")
//                        trackListViewModel.swipeRight()
//                    },
//                    onSwipeLeft = {
//                        Log.d(TAG, "onSwipeLeft current: ")
//                        trackListViewModel.swipeLeft()
//                    }
//                ) {
//                    TrackCard(it)
//                }
//            }
        }
    }
    LazyColumn {  }
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