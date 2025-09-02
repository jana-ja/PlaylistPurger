package de.janaja.playlistpurger.features.vote_result.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.core.ui.component.CollapsableTopAppBar
import de.janaja.playlistpurger.core.ui.component.DataStateView
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.features.vote_result.presentation.component.TrackVoteResultItem
import de.janaja.playlistpurger.features.vote_result.presentation.component.VoteResultHeader
import de.janaja.playlistpurger.features.vote_result.presentation.viewmodel.VoteResultViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoteResultScreen(
    modifier: Modifier = Modifier,
    voteResultViewModel: VoteResultViewModel = koinViewModel()
) {

    val playlistName = voteResultViewModel.playlistName
    val dataState by voteResultViewModel.dataState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    // song mit den vote dingern mit anzahl der votes, wenn alle abgestimmt haben ist grün hinterlegt oder so
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsableTopAppBar (
                title = { Text(playlistName) },
                scrollBehavior = scrollBehavior,
                contentMaxHeight = 125.dp
            ) {
                val state = dataState
                if (state is DataState.Ready)
                    VoteResultHeader(
                        playlistVoteResults = state.data,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            DataStateView(
                dataState
            ) { data ->

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(data.tracksWithVotes) { (track, voteResult) ->
                        TrackVoteResultItem(
                            track = track,
                            voteResult = voteResult
                        )
                    }
                }
            }
        }
    }
}