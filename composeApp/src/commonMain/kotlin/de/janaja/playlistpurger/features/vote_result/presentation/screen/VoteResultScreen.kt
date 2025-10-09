package de.janaja.playlistpurger.features.vote_result.presentation.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.core.ui.component.CollapsableTopAppBar
import de.janaja.playlistpurger.core.ui.component.DataStateView
import de.janaja.playlistpurger.core.ui.component.SearchTextField
import de.janaja.playlistpurger.core.ui.model.DataState
import de.janaja.playlistpurger.core.ui.util.UiText
import de.janaja.playlistpurger.features.vote_result.presentation.component.SaveRemoveSongsDialog
import de.janaja.playlistpurger.features.vote_result.presentation.component.TrackVoteResultItem
import de.janaja.playlistpurger.features.vote_result.presentation.component.VoteResultHeader
import de.janaja.playlistpurger.features.vote_result.presentation.viewmodel.VoteResultViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_save_24

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VoteResultScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    voteResultViewModel: VoteResultViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        voteResultViewModel.toast.collect { text ->
            snackbarHostState.showSnackbar(
                message = text
            )
        }
    }

    val playlistName = voteResultViewModel.playlistName
    val dataState by voteResultViewModel.dataState.collectAsState()
    val searchQuery by voteResultViewModel.searchQuery.collectAsState()
    val activeFilters by voteResultViewModel.activeFilters.collectAsState()
    val allPlaylists by voteResultViewModel.allPlaylists.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    // song mit den vote dingern mit anzahl der votes, wenn alle abgestimmt haben ist grün hinterlegt oder so
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsableTopAppBar(
                title = { Text(playlistName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "navigate bacl")
                    }
                },
                scrollBehavior = scrollBehavior,
                contentMaxHeight = 170.dp // 125 war vorher, jetzt + 56 von searchbar
            ) {
                val state = dataState
                if (state is DataState.Ready) {
                    Column {
                        SearchTextField(
                            searchText = searchQuery,
                            onSearchTextChange = voteResultViewModel::onSearchTextChange,
                            onSearchImeAction = { },
                            modifier = Modifier.fillMaxWidth()
                        )
                        VoteResultHeader(
                            state.data.collaborators,
                            state.data.stats,
                            onClickUser = voteResultViewModel::onClickUserHeader,
                            onClickVoteOption = voteResultViewModel::onClickVoteOptionHeader,
                            activeFilters = activeFilters,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Row {
                            Text(
                                "${state.data.tracksWithVoteResult.size}",
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    showSaveDialog = true
                                },
                            ) {
                                Icon(painter = painterResource(Res.drawable.baseline_save_24), contentDescription = "Save remove candidates")
                            }
                        }
                    }
                }
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
                    items(data.tracksWithVoteResult) { (track, voteResult) ->
                        TrackVoteResultItem(
                            track = track,
                            voteResult = voteResult
                        )
                    }
                }
            }

            if (showSaveDialog) {
                SaveRemoveSongsDialog(
                    onDismiss = {
                        showSaveDialog = false
                    },
                    onSave = {
                        voteResultViewModel.saveRemoveCandidates(it)
                    },
                    playlists = allPlaylists
                )
            }
        }
    }
}