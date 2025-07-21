package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.ui.component.DataStateView
import de.janaja.playlistpurger.ui.component.PlaylistItem
import de.janaja.playlistpurger.ui.component.SearchTextField
import de.janaja.playlistpurger.ui.viewmodel.PlaylistOverviewViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaylistOverviewScreen(
    onNavToTrackList: (String, String) -> Unit,
    onNavToResult: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistOverviewViewModel = koinViewModel()
) {
    val dataState by viewModel.dataState.collectAsState()
    val searchText by viewModel.searchQuery.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SearchTextField(
            searchText = searchText,
            onSearchTextChange = { viewModel.onSearchTextChange(it) },
            onSearchImeAction = { },
            modifier = Modifier.fillMaxWidth()
        )

        DataStateView(dataState) { data ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(data, key = {it.id}) {
                    PlaylistItem(
                        it,
                        onNavToVote = onNavToTrackList,
                        onNavToResult = onNavToResult,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }

    }
}