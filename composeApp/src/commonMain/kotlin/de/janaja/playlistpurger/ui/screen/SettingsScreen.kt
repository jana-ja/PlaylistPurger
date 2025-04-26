package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import de.janaja.playlistpurger.R
import de.janaja.playlistpurger.ui.viewmodel.SettingsViewModel
import me.zhanghai.compose.preference.ProvidePreferenceLocals
import me.zhanghai.compose.preference.SwitchPreference
import me.zhanghai.compose.preference.TwoTargetIconButtonPreference
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val showSwipeFirst by settingsViewModel.showSwipeFirst.collectAsState()
    ProvidePreferenceLocals {
        LazyColumn {
            item {
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
            }
            item {
                TwoTargetIconButtonPreference(
                    title = {
                        Text("Logout")
                    },
                    iconButtonIcon = {
                        Icon(
                            painterResource(R.drawable.baseline_logout_24),
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
}