package de.janaja.playlistpurger.ui.screen.vote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.domain.model.Track
import de.janaja.playlistpurger.domain.model.VoteOption
import de.janaja.playlistpurger.ui.component.SwipeCard
import de.janaja.playlistpurger.ui.component.SwipeCardState
import de.janaja.playlistpurger.ui.component.SwipeDirection
import de.janaja.playlistpurger.ui.component.TrackCard
import de.janaja.playlistpurger.ui.component.VoteButton
import de.janaja.playlistpurger.ui.component.rememberSwipeCardState
import kotlinx.coroutines.launch

@Composable
fun SwipeView(
    swipeableTracks: List<Track>,
    onSwipe: (SwipeDirection, Track) -> Unit,
    onSwitchSwipeMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var topSwipeCardState: SwipeCardState? = null
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Alle Songs wurden gevotet")
            FilledTonalButton(onClick = onSwitchSwipeMode) { Text("Zur Listenansicht") }
        }

        swipeableTracks.reversed().forEachIndexed { index, track ->
            key(track.id) {
                val br = rememberSwipeCardState()
                if (index == swipeableTracks.lastIndex) {
                    topSwipeCardState = br
                }
                SwipeCard(
                    swipeCardState = br,
                    onSwiped = { onSwipe(it, track) },
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
                            it.swipe(vote.swipeDirection)
                        }
                    },
                    iconResId = vote.resource,
                    selectionColor = vote.color,
                    selectionScaling = 1.5f,
                    contentDescription = vote.contentDescription,
                )
            }
        }
    }
}