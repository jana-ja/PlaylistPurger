package de.janaja.playlistpurger.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import de.janaja.playlistpurger.data.PreviewData.previewTrack
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSwipeableCardApi::class)
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

//    val swipeStates by remember (swipableTracks) {
//        derivedStateOf { items ->
//            items.map {
//                it to rememberSwipeableCardState()
//            }
//        }
//    }
    var swipeStates = swipableTracks
        .map {
            Log.d(TAG, "TrackListScreen: mappi in screen")
            it to rememberSwipeableCardState()
        }
        .onEach { he ->
            Log.d(TAG, "unvotedTracks: updatet ${he.first.name}")
        }

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

            swipeStates.reversed().forEach { (track, state) ->
//                if (state.swipedDirection == null) {
//                key(track.id) {
                TrackCard(track,
                    modifier = Modifier.swipableCard(
                        state = state,
                        onSwiped = { direction ->
                            when (direction) {
                                Direction.Left -> {
                                    trackListViewModel
                                        .swipeLeft(track)
                                }

                                Direction.Right -> {
                                    trackListViewModel
                                        .swipeRight(track)
                                }

                                Direction.Up -> {}
                                Direction.Down -> {}
                            }
                        }
                    )
                )
//                }
//                }
            }
        }
            Box {
            swipableTracks.reversed().forEach {
                key(it.id) {
                    SwipeCard(
                        id = it.name,
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