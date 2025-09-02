package de.janaja.playlistpurger.features.playlist_overview.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import de.janaja.playlistpurger.core.ui.component.SearchTextField
import de.janaja.playlistpurger.features.playlist_overview.presentation.component.PlaylistItem
import de.janaja.playlistpurger.features.playlist_overview.presentation.viewmodel.PlaylistOverviewViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistOverviewScreen(
    onNavToTrackList: (String, String) -> Unit,
    onNavToResult: (String, String) -> Unit,
    onNavToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaylistOverviewViewModel = koinViewModel()
) {
    val dataState by viewModel.dataState.collectAsState()
    val searchText by viewModel.searchQuery.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsableTopAppBar(
                title = { Text("All Playlists") },
                actions = {
                    IconButton(onClick = onNavToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            "Settings"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                contentMaxHeight = 72.dp
            ) {
                SearchTextField(
                    searchText = searchText,
                    onSearchTextChange = { viewModel.onSearchTextChange(it) },
                    onSearchImeAction = { },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            DataStateView(dataState) { data ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(top = 8.dp)
                ) {
                    items(data, key = { it.id }) {
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
}