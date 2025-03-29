package de.janaja.playlistpurger

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity.REQUEST_CODE
import de.janaja.playlistpurger.ui.screen.WelcomeScreen
import de.janaja.playlistpurger.ui.theme.PlaylistPurgerTheme
import de.janaja.playlistpurger.ui.viewmodel.AuthViewModel
import de.janaja.playlistpurger.ui.viewmodel.DasViewModel


class MainActivity : ComponentActivity() {
    val TAG = "MainActivity"


    var token: String? = null
    val viewModel = DasViewModel()

    lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")
        val bla =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

                Log.d(TAG, "onCreate: in register for activity result")
                // Check if result comes from the correct activity
                if (result.resultCode == RESULT_OK && result.data != null) {
                    // apparently don't need to check with request code if ActivityResult comes from spotify's login activity
//                    val resultCode = result.resultCode
//                    val data = result.data
                    val response: AuthorizationResponse =
                        AuthorizationClient.getResponse(result.resultCode, result.data)
//                    result
                    when (response.type) {
                        // Response was successful and contains auth token
                        AuthorizationResponse.Type.TOKEN -> {
                            println("Success! ${AuthorizationResponse.Type.TOKEN}")
                        }
                        // Auth flow returned an error
                        AuthorizationResponse.Type.ERROR -> {
                            println("Error")
                            println(AuthorizationResponse.Type.ERROR)
                        }
                        // Most likely auth flow was cancelled
                        else -> {
                            println("Auth flow canceled")
                        }
                    }
                } else {
                    println("No result returned")
                }
            }



        authViewModel = AuthViewModel(
            onStartLoginActivity = { requestCode, request ->
                val intent = AuthorizationClient.createLoginActivityIntent(this, request)
                bla.launch(intent)

            }
        )

        enableEdgeToEdge()
        setContent {

            PlaylistPurgerTheme {


                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WelcomeScreen(
                        onLogin = {
                            authViewModel.login()

                        },
                        modifier = Modifier.padding(innerPadding),
                        welcomeViewModel = viewModel
                    )
                }
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d(TAG, "onActivityResult: hää $requestCode, $resultCode, $data")
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            Log.d(TAG, "onActivityResult: ist von spotify")
            val response = AuthorizationClient.getResponse(resultCode, data)
            Log.d(TAG, "onActivityResult: $response")
            when (response.type) {
                AuthorizationResponse.Type.CODE -> {
                    Log.d(TAG, "onActivityResult: ${response.code}")
                    response.code
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.d(TAG, "onActivityResult: error")
                }
                AuthorizationResponse.Type.TOKEN -> {
                    Log.d(TAG, "onActivityResult: token")
                    token = response.accessToken
                    viewModel.updateToken(response.accessToken)
                }
                else -> {
                    Log.d(TAG, "onActivityResult: else")
                    Log.d(TAG, "onActivityResult: ${response.type}")
                }
            }
        }
    }

}