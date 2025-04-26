package de.janaja.playlistpurger.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SwitchPreference(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    title: @Composable () -> Unit,
    summary: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            title()
            summary()
        }

        Spacer(Modifier.weight(1f))

        Switch(
            checked = value,
            onCheckedChange = onValueChange
        )
    }
}