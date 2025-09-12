package de.janaja.playlistpurger.features.settings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.features.settings.presentation.component.IconButtonPreference
import de.janaja.playlistpurger.features.settings.presentation.component.SwitchPreference
import de.janaja.playlistpurger.features.settings.presentation.viewmodel.SettingsViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_logout_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val settings by settingsViewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "navigate bacl")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier.padding(innerPadding)
        ) {
            SwitchPreference(
                value = settings.showSwipeFirst,
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
}




