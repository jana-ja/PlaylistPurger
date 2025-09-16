package de.janaja.playlistpurger.features.track_voting.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.core.ui.component.DataStateView
import de.janaja.playlistpurger.core.ui.component.IconSwitch
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.features.track_voting.presentation.component.PlayerControls
import de.janaja.playlistpurger.features.track_voting.presentation.component.SwipeVoteTrackStack
import de.janaja.playlistpurger.features.track_voting.presentation.component.TrackItem
import de.janaja.playlistpurger.features.track_voting.presentation.viewmodel.TrackListVoteViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackListVoteScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    trackListVoteViewModel: TrackListVoteViewModel = koinViewModel()
) {

    val playlistName = trackListVoteViewModel.playlistName
    val swipeModeOn by trackListVoteViewModel.swipeModeOn.collectAsState()

    val dataState by trackListVoteViewModel.dataState.collectAsState()

    val swipeableTracks by trackListVoteViewModel.swipeTracks.collectAsState()
    val allTracksCount by trackListVoteViewModel.allTracksCount.collectAsState(0)
    val votedTracksCount by trackListVoteViewModel.votedTracksCount.collectAsState(0)

    val isPlaying by trackListVoteViewModel.isPlaying.collectAsState()

    val selectedDevice by trackListVoteViewModel.selectedDevice.collectAsState()
    val availableDevices by trackListVoteViewModel.availableDevices.collectAsState()

    // TODO global player state?
    DisposableEffect(Unit) {
        onDispose {
            if (isPlaying)
                trackListVoteViewModel.playPauseSwipeTrack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "navigate bacl")
                    }
                },
                title = { Text(playlistName) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                if (dataState is DataState.Ready) {
                    Text("$votedTracksCount/$allTracksCount")
                }
                Spacer(Modifier.weight(1f))
                IconSwitch(
                    checked = swipeModeOn,
                    onCheckedChange = { trackListVoteViewModel.switchSwipeMode(it) }
                )
            }

            DataStateView(dataState) { data ->
                if (swipeModeOn) {
                    SwipeVoteTrackStack(
                        swipeableTracks = swipeableTracks,
                        onSwipe = { dir, track ->
                            trackListVoteViewModel.onSwipe(dir, track)
                        },
                        onSwitchSwipeMode = {
                            trackListVoteViewModel.switchSwipeMode(false)
                        }
                    )

                    Spacer(Modifier.weight(1f))

                    PlayerControls(
                        selectedDevice = selectedDevice,
                        availableDevices = availableDevices,
                        onDeviceChange = {
                            trackListVoteViewModel.onDeviceChange(it)
                        },
                        onClickPlayPause = {
                            trackListVoteViewModel.playPauseSwipeTrack()
                        },
                        onClickForward = {
                            trackListVoteViewModel.forwardTrack()
                        },
                        onClickRewind = {
                            trackListVoteViewModel.rewindTrack()
                        },
                        isPlaying = isPlaying,
                        track = swipeableTracks.firstOrNull(),
                    )

                    Spacer(Modifier.height(24.dp))

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
}