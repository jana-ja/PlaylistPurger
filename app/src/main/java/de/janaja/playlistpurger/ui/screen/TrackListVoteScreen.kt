package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.ui.component.DataStateView
import de.janaja.playlistpurger.ui.component.IconSwitch
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.SwipeCardState
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.component.TrackItem
import de.janaja.playlistpurger.ui.component.VoteButton
import de.janaja.playlistpurger.ui.component.rememberSwipeCardState
import de.janaja.playlistpurger.ui.viewmodel.TrackListVoteViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrackListVoteScreen(
    modifier: Modifier = Modifier,
    trackListVoteViewModel: TrackListVoteViewModel = koinViewModel()
) {

    val swipeModeOn by trackListVoteViewModel.swipeModeOn.collectAsState()

    val dataState by trackListVoteViewModel.dataState.collectAsState()

    // swipe
    // TODO add to data state?
    val swipeableTracks by trackListVoteViewModel.swipeTracks.collectAsState(
        emptyList()
    )
    var topSwipeCardState: SwipeCardState? = null
    val scope = rememberCoroutineScope()

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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Alle Songs wurden gevotet")
                        FilledTonalButton(onClick = {
                            trackListVoteViewModel.switchSwipeMode(false)
                        }) { Text("Zur Listenansicht") }
                    }

                    swipeableTracks.reversed().forEachIndexed { index, track ->
                        key(track.id) {
                            val br = rememberSwipeCardState()
                            if (index == swipeableTracks.lastIndex) {
                                topSwipeCardState = br
                            }
                            SwipeCard(
                                swipeCardState = br,
                                onSwiped = { dir ->
                                    trackListVoteViewModel.onSwipe(dir, track)

                                },
                            ) {
                                TrackCard(
                                    track,
                                    modifier = Modifier.fillMaxWidth()
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
                                selected = (vote == it.currentSwipeDirection?.let { it1 ->
                                    VoteOption.fromSwipeDirection(it1)
                                }),
                                onClick = {
                                    scope.launch {
                                        it.swipe(vote.getSwipeDirection())
                                    }
                                },
                                iconResId = vote.imgResId,
                                selectionColor = vote.color,
                                selectionScaling = 1.5f,
                                contentDescription = vote.contentDescription,
                            )
                        }
                    }
                }
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