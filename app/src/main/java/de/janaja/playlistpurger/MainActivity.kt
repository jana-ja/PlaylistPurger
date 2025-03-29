package de.janaja.playlistpurger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.spotify.sdk.android.auth.AuthorizationClient
import de.janaja.playlistpurger.ui.screen.WelcomeScreen
import de.janaja.playlistpurger.ui.theme.PlaylistPurgerTheme
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel


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
            onStartLoginActivity = { requestCode, request ->
                val intent = AuthorizationClient.createLoginActivityIntent(this, request)
                activityResultLauncher.launch(intent)

            }
        )
        enableEdgeToEdge()
        setContent {
            PlaylistPurgerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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