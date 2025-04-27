package de.janaja.playlistpurger

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.ui.AppStart
import de.janaja.playlistpurger.ui.screen.SplashScreen
import de.janaja.playlistpurger.ui.screen.WelcomeScreen
import de.janaja.playlistpurger.ui.theme.PlaylistPurgerTheme
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val authViewModel: AuthViewModel = koinViewModel()

    val loginState by authViewModel.loginState.collectAsState()

    PlaylistPurgerTheme {
        when (loginState) {
            LoginState.Loading -> {
                SplashScreen()
            }

            is LoginState.LoggedIn -> {
                AppStart()
            }

            LoginState.LoggedOut -> {
                WelcomeScreen(
                    modifier = Modifier.statusBarsPadding()
                )
            }
        }
    }
}