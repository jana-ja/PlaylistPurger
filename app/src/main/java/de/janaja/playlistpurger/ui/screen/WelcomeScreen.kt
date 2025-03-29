package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onLogin) { Text("login") }
    }

}

@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    // Use Theme here
    WelcomeScreen({})
}