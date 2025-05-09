package de.janaja.playlistpurger.ui.screen.vote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.ui.component.DataStateView
import de.janaja.playlistpurger.ui.component.IconSwitch
import de.janaja.playlistpurger.ui.component.TrackItem
import de.janaja.playlistpurger.ui.viewmodel.TrackListVoteViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackListVoteScreen(
    modifier: Modifier = Modifier,
    trackListVoteViewModel: TrackListVoteViewModel = koinViewModel()
) {

    val swipeModeOn by trackListVoteViewModel.swipeModeOn.collectAsState()

    val dataState by trackListVoteViewModel.dataState.collectAsState()

    val swipeableTracks by trackListVoteViewModel.swipeTracks.collectAsState(
        emptyList()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Spacer(Modifier.weight(1f))
            IconSwitch(
                checked = swipeModeOn,
                onCheckedChange = { trackListVoteViewModel.switchSwipeMode(it) }
            )
        }

        DataStateView(dataState) { data ->
            if (swipeModeOn) {
                SwipeView(
                    swipeableTracks = swipeableTracks,
                    onSwipe = { dir, track ->
                        trackListVoteViewModel.onSwipe(dir, track)
                    },
                    onSwitchSwipeMode = {
                        trackListVoteViewModel.switchSwipeMode(false)
                    }
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(data) { track ->
                        TrackItem(track, onChangeVote = { newVote ->
                            trackListVoteViewModel.onChangeVote(track, newVote)
                        })
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun TrackListScreenPreview() {
    // Use Theme here
    TrackListVoteScreen()
}