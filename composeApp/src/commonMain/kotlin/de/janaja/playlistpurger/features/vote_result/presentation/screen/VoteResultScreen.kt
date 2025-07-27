package de.janaja.playlistpurger.features.vote_result.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.core.ui.component.DataStateView
import de.janaja.playlistpurger.features.vote_result.presentation.component.TrackVoteResultItem
import de.janaja.playlistpurger.features.vote_result.presentation.viewmodel.VoteResultViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VoteResultScreen(
    modifier: Modifier = Modifier,
    voteResultViewModel: VoteResultViewModel = koinViewModel()
) {

    val dataState by voteResultViewModel.dataState.collectAsState()
    // song mit den vote dingern mit anzahl der votes, wenn alle abgestimmt haben ist grün hinterlegt oder so

    Column(modifier = modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        DataStateView(
            dataState
        ) { data ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(data) { (track, voteList) ->
                    TrackVoteResultItem(track, voteList)
                }
            }
        }
    }
}