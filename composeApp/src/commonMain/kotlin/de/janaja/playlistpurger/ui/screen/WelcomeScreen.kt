package de.janaja.playlistpurger.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = koinViewModel()
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val uriHandler = LocalUriHandler.current
        Button(onClick = {
            authViewModel.startLoginProcess(uriHandler)

        }) { Text("login") }
    }
}