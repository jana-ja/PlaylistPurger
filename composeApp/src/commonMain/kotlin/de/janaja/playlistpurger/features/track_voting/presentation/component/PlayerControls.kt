package de.janaja.playlistpurger.features.track_voting.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.features.player.domain.model.Device
import de.janaja.playlistpurger.shared.domain.model.Track
import org.jetbrains.compose.resources.painterResource
import playlistpurger.composeapp.generated.resources.Res
import playlistpurger.composeapp.generated.resources.baseline_forward_10_24
import playlistpurger.composeapp.generated.resources.baseline_pause_24
import playlistpurger.composeapp.generated.resources.baseline_play_arrow_24
import playlistpurger.composeapp.generated.resources.baseline_replay_10_24
import playlistpurger.composeapp.generated.resources.baseline_speaker_24
import playlistpurger.composeapp.generated.resources.outline_refresh_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO add own viewmodel, only pass track?
//  or move device selection to own component?
fun PlayerControls(
    selectedDevice: Device?,
    availableDevices: List<Device>,
    onDeviceChange: (Device) -> Unit,
    onRefreshDevices: () -> Unit,
    track: Track?,
    isPlaying: Boolean,
    onClickPlayPause: () -> Unit,
    onClickForward: () -> Unit,
    onClickRewind: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deviceDropDownExpanded by remember { mutableStateOf(false) }
    val enabled = (track != null && selectedDevice != null)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier.size(40.dp).padding(2.dp),
            onClick = {
                deviceDropDownExpanded = true
            },
//            colors = IconButtonDefaults.iconButtonColors(containerColor = Color(0xFF0000FF))
        ) {
            Icon(
                painter = painterResource(Res.drawable.baseline_speaker_24),
                contentDescription = "Open device menu",
                tint = if (selectedDevice != null) Color(0xFF00FF00) else Color(0xFFFF0000),
                modifier = Modifier.fillMaxSize()
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onClickRewind,
                enabled = enabled
            ) {
                Icon(
                    painter = painterResource(Res.drawable.baseline_replay_10_24),
                    contentDescription = "Rewind Track 10 seconds",
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = onClickPlayPause,
                enabled = enabled
            ) {
                val imgRes = if (isPlaying) Res.drawable.baseline_pause_24 else Res.drawable.baseline_play_arrow_24
                val desc = if (isPlaying) "Pause Track ${track?.name}" else "Play Track ${track?.name}"

                Icon(
                    painter = painterResource(imgRes),
                    contentDescription = desc,
                    modifier = Modifier.size(40.dp)
                )
            }

            IconButton(
                onClick = onClickForward,
                enabled = enabled
            ) {
                Icon(
                    painter = painterResource(Res.drawable.baseline_forward_10_24),
                    contentDescription = "Forward Track 10 seconds",
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Box(Modifier.size(40.dp))
    }

    if (deviceDropDownExpanded) {
        ModalBottomSheet(
            onDismissRequest = {
                deviceDropDownExpanded = false
            }
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Available Devices:",
                        style = MaterialTheme.typography.titleLarge
                    )
                    IconButton(
                        onClick = onRefreshDevices
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.outline_refresh_24),
                            contentDescription = "Refresh Devices"
                        )
                    }
                }

                if (availableDevices.isEmpty()) {
                    Text(
                        "No devices available. Please open Spotify on any connected device and refresh",
                        style = MaterialTheme.typography.labelLarge
                    )
                } else {
                    if (selectedDevice == null) {
                        Text(
                            "Please select a device",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Column(modifier.selectableGroup()) {
                        availableDevices.forEach { device ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (device.id == selectedDevice?.id),
                                        onClick = { onDeviceChange(device) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (device.id == selectedDevice?.id),
                                    onClick = null // null recommended for accessibility with screen readers
                                )
                                Text(
                                    text = device.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}