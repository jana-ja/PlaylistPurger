package de.janaja.playlistpurger.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.janaja.playlistpurger.features.playlist_overview.presentation.screen.PlaylistOverviewScreen
import de.janaja.playlistpurger.features.settings.presentation.screen.SettingsScreen
import de.janaja.playlistpurger.features.track_voting.presentation.screen.TrackListVoteScreen
import de.janaja.playlistpurger.features.vote_result.presentation.screen.VoteResultScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = PlaylistOverviewRoute,
        modifier = modifier
    ) {
        composable<PlaylistOverviewRoute> {
            PlaylistOverviewScreen(
                onNavToTrackList = { playlistId, playlistName ->
                    navController.navigate(TrackListRoute(playlistId, playlistName))
                },
                onNavToResult = { playlistId, playlistName ->
                    navController.navigate(VoteResultRoute(playlistId, playlistName))
                },
                onNavToSettings = {
                    navController.navigate(SettingsRoute)
                }
            )
        }

        composable<MenuRoute> {

        }

        composable<TrackListRoute> {
            TrackListVoteScreen()
        }

        composable<VoteResultRoute> {
            VoteResultScreen()
        }

        composable<SettingsRoute> {
            SettingsScreen()
        }

    }

}