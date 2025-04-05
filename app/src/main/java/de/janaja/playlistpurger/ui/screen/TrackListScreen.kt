package de.janaja.playlistpurger.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import de.janaja.playlistpurger.data.model.VoteOption
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.SwipeCardState
import de.janaja.playlistpurger.ui.component.SwipeDirection
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.component.VoteButton
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
                        TrackCard(
                            it,
                            modifier = Modifier.size(400.dp)
                        )
                    }
                }
            }
        }

        topSwipeCardState?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                VoteOption.entries.reversed().forEach { vote ->
                    VoteButton(
                        selected = (when (it.currentSwipeDirection) {
                            SwipeDirection.Up -> vote == VoteOption.DONT_CARE
                            SwipeDirection.Left -> vote == VoteOption.REMOVE
                            SwipeDirection.Right -> vote == VoteOption.KEEP
                            else -> false

                        }),
                        onClick = {
                            scope.launch {
                                it.swipe(
                                    when (vote) {
                                        VoteOption.KEEP -> SwipeDirection.Right
                                        VoteOption.DONT_CARE -> SwipeDirection.Up
                                        VoteOption.REMOVE -> SwipeDirection.Left
                                    }
                                )
                            }
                        },
                        iconResId = vote.imgResId,
                        selectionColor = vote.color,
                        contentDescription = vote.contentDescription,
                    )
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