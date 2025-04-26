package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.ui.component.IconButtonPreference
import de.janaja.playlistpurger.ui.component.SwitchPreference
import de.janaja.playlistpurger.ui.viewmodel.SettingsViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_logout_24

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val showSwipeFirst by settingsViewModel.showSwipeFirst.collectAsState()
    Column(
        modifier
    ) {
        SwitchPreference(
            value = showSwipeFirst,
            onValueChange = {
                settingsViewModel.updateShowSwipeFirst(it)
            },
            title = { Text("Prefer Swipe View") },
            summary = {
                Text("When turned on, Swipe View is preferred over List View in Vote Screen ")
            }
        )
        IconButtonPreference(
            title = {
                Text("Logout")
            },
            iconButtonIcon = {
                Icon(
                    painterResource(Res.drawable.baseline_logout_24),
                    "logout"
                )
            },
            summary = {
                Text("Log out off your Spotify Account")
            }
        ) {
            settingsViewModel.logout()
        }
    }
}




