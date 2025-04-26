package de.janaja.playlistpurger

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import de.janaja.playlistpurger.domain.model.LoginState
import de.janaja.playlistpurger.ui.AppStart
import de.janaja.playlistpurger.ui.screen.SplashScreen
import de.janaja.playlistpurger.ui.screen.WelcomeScreen
import de.janaja.playlistpurger.ui.theme.PlaylistPurgerTheme
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

//import com.spotify.sdk.android.auth.AuthorizationRequest
//import com.spotify.sdk.android.auth.AuthorizationClient

@Composable
@Preview
fun App() {
    val authViewModel: AuthViewModel = koinViewModel(
//        parameters = {
//            org.koin.core.parameter.parametersOf(
//                { request: AuthorizationRequest ->
//                    val intent =
//                        AuthorizationClient.createLoginActivityIntent(this, request)
//                    activityResultLauncher.launch(intent)
//                }
//            )
//        }
    )

    val loginState by authViewModel.loginState.collectAsState()
//            val isLoading by authViewModel.isLoading.collectAsState()
//            val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    PlaylistPurgerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            when (loginState) {
                LoginState.Loading -> {
                    SplashScreen()
                }

                is LoginState.LoggedIn -> {
                    AppStart()
                }

                LoginState.LoggedOut -> {
                    WelcomeScreen(
                        onLogin = {
                            authViewModel.startLoginProcess()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
}