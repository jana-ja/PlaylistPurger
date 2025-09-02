package de.janaja.playlistpurger.features.auth.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import de.janaja.playlistpurger.features.auth.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = koinViewModel()
) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val uriHandler = LocalUriHandler.current
            Button(onClick = {
                authViewModel.startLoginProcess(uriHandler)

            }) { Text("login") }
        }
    }
}