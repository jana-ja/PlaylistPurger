package de.janaja.playlistpurger.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.ui.screen.PlaylistOverviewScreen
import de.janaja.playlistpurger.ui.screen.TrackListScreen
import de.janaja.playlistpurger.ui.screen.VoteResultScreen
import kotlinx.serialization.Serializable

@Serializable
object MenuRoute

@Serializable
object PlaylistOverviewRoute

@Serializable
data class TrackListRoute(
    val playlistId: String,
    val playlistName: String
)

@Serializable
data class VoteResultRoute(
    val playlistId: String,
    val playlistName: String
)

data class TopBarUiState(
    val title: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppStart(
    onLogOut: () -> Unit
) {
    val navController = rememberNavController()
    var topBarUiState by remember { mutableStateOf(TopBarUiState("Playlist Purger")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarUiState.title) },
                actions = {
                    IconButton(onClick = onLogOut) {
                        Icon(
                            painterResource(R.drawable.baseline_logout_24),
                            "Logout"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PlaylistOverviewRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<PlaylistOverviewRoute> {
                topBarUiState = TopBarUiState("All Playlists")
                PlaylistOverviewScreen(
                    onNavToTrackList = { playlistId, playlistName ->
                        navController.navigate(TrackListRoute(playlistId, playlistName))
                    },
                    onNavToResult = { playlistId, playlistName ->
                        navController.navigate(VoteResultRoute(playlistId, playlistName))
                    }
                )
            }

            composable<MenuRoute> {

            }

            composable<TrackListRoute> { iwas ->
                val name = iwas.toRoute<TrackListRoute>().playlistName
                topBarUiState = TopBarUiState(name)
                TrackListScreen()
            }

            composable<VoteResultRoute> { iwas ->
                val name = iwas.toRoute<VoteResultRoute>().playlistName
                topBarUiState = TopBarUiState(name)
                VoteResultScreen()
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppStartPreview() {
    // Use Theme here
    AppStart({})
}