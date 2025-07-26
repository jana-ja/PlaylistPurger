package de.janaja.playlistpurger.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import de.janaja.playlistpurger.core.ui.MenuRoute
import de.janaja.playlistpurger.core.ui.PlaylistOverviewRoute
import de.janaja.playlistpurger.core.ui.SettingsRoute
import de.janaja.playlistpurger.core.ui.TopBarUiState
import de.janaja.playlistpurger.core.ui.TrackListRoute
import de.janaja.playlistpurger.core.ui.VoteResultRoute
import de.janaja.playlistpurger.features.playlist_overview.presentation.screen.PlaylistOverviewScreen
import de.janaja.playlistpurger.features.settings.presentation.screen.SettingsScreen
import de.janaja.playlistpurger.features.track_voting.presentation.screen.TrackListVoteScreen
import de.janaja.playlistpurger.features.vote_result.presentation.screen.VoteResultScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    updateTopBar: (TopBarUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PlaylistOverviewRoute,
        modifier = modifier
    ) {
        composable<PlaylistOverviewRoute> {
            updateTopBar(TopBarUiState("All Playlists")
            )
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
            updateTopBar(TopBarUiState(name))
            TrackListVoteScreen()
        }

        composable<VoteResultRoute> { iwas ->
            val name = iwas.toRoute<VoteResultRoute>().playlistName
            updateTopBar(TopBarUiState(name))
            VoteResultScreen()
        }

        composable<SettingsRoute> {
            updateTopBar(
                TopBarUiState("Settings")
            )
            SettingsScreen()
        }

    }

}