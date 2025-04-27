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
import de.janaja.playlistpurger.ui.viewmodel.LoginResult
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    loginResult: LoginResult?
) {
    val authViewModel: AuthViewModel = koinViewModel()

    val loginState by authViewModel.loginState.collectAsState()

    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            authViewModel.handleLoginResult(loginResult)
        }
    }

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