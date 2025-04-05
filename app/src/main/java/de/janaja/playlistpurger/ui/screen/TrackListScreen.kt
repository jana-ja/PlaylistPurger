package de.janaja.playlistpurger.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.SwipeCardState
import de.janaja.playlistpurger.ui.component.SwipeDirection
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.component.rememberSwipeCardState
import de.janaja.playlistpurger.ui.viewmodel.TrackListViewModel
import kotlinx.coroutines.launch
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

    var topSwipeCardState: SwipeCardState? = null
    val scope = rememberCoroutineScope()

    var swipeStates = swipeableTracks
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
        ) {
            Text("Keine SOngs Ürbig wechsel zu liste")

            swipeableTracks.reversed().forEachIndexed { index, it ->
                key(it.id) {
                    val br = rememberSwipeCardState()
                    if (index == swipeableTracks.lastIndex) {
                        topSwipeCardState = br
                    }
                    SwipeCard(
                        swipeCardState = br,
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

            Button(onClick = {
                scope.launch {

                    topSwipeCardState?.swipe(SwipeDirection.Up)
                }
            }) { Text("swipe up") }
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