package de.janaja.playlistpurger.core.ui

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.core.ui.navigation.AppNavHost
import de.janaja.playlistpurger.features.auth.domain.model.UserLoginState
import de.janaja.playlistpurger.features.auth.presentation.screen.WelcomeScreen
import de.janaja.playlistpurger.core.ui.theme.PlaylistPurgerTheme
import de.janaja.playlistpurger.features.auth.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppEntry() {
    val authViewModel: AuthViewModel = koinViewModel()

    val loginState by authViewModel.userLoginState.collectAsState()

    PlaylistPurgerTheme {
        when (loginState) {
            UserLoginState.Loading -> {
                SplashScreen()
            }

            is UserLoginState.LoggedIn -> {
                AppNavHost()
            }

            UserLoginState.LoggedOut -> {
                WelcomeScreen(
                    modifier = Modifier.statusBarsPadding()
                )
            }
        }
    }
}