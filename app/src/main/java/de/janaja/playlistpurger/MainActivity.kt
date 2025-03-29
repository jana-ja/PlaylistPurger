package de.janaja.playlistpurger

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.spotify.sdk.android.auth.AuthorizationClient
import de.janaja.playlistpurger.ui.screen.WelcomeScreen
import de.janaja.playlistpurger.ui.theme.PlaylistPurgerTheme
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import de.janaja.playlistpurger.ui.AppStart
import de.janaja.playlistpurger.ui.screen.PlaylistOverviewScreen
import de.janaja.playlistpurger.ui.screen.SplashScreen

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "playlist_purger_datastore")

object DatastoreKeys {
    val accessToken by lazy { stringPreferencesKey("accessToken") }
}

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivity"
    private lateinit var authViewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                authViewModel.handleLoginResult(result)
            }

        authViewModel = AuthViewModel(
            application,
            onStartLoginActivity = { request ->
                val intent = AuthorizationClient.createLoginActivityIntent(this, request)
                activityResultLauncher.launch(intent)
            }
        )
        enableEdgeToEdge()
        setContent {
            val isLoading by authViewModel.isLoading.collectAsState()
            val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

            PlaylistPurgerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isLoading) {
                        SplashScreen()
                    } else {
                        if (isLoggedIn) {
                            AppStart {
                                authViewModel.logout()
                            }
                        } else {
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
        }
    }
}