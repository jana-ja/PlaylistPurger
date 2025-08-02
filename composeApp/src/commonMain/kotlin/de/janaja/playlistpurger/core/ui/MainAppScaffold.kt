package de.janaja.playlistpurger.core.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import de.janaja.playlistpurger.core.ui.navigation.AppNavHost
import kotlinx.serialization.Serializable

@Serializable
object MenuRoute

@Serializable
object PlaylistOverviewRoute

@Serializable
object SettingsRoute

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
fun MainAppScaffold() {
    val navController = rememberNavController()
    var topBarUiState by remember { mutableStateOf(TopBarUiState("Playlist Purger")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarUiState.title) },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(SettingsRoute)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            "Settings"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            updateTopBar = { newState ->
                topBarUiState = newState
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}