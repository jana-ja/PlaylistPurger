package de.janaja.playlistpurger.features.settings.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun IconButtonPreference(
    title: @Composable () -> Unit,
    iconButtonIcon: @Composable () -> Unit,
    summary: @Composable () -> Unit,
    action: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            title()
            summary()
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = action,
            content = iconButtonIcon
        )
    }
}
