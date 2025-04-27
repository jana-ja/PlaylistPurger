package de.janaja.playlistpurger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.janaja.playlistpurger.ui.viewmodel.LoginResult


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // when activity is started from spotify login website -> intent contains response data
        val intentData = intent.data
        val loginResult: LoginResult? = intentData?.let {
            it.getQueryParameter("code")?.let { code ->
                LoginResult.SuccessLoginResult(code)
            } ?: it.getQueryParameter("error")?.let { error ->
                LoginResult.ErrorLoginResult(error)
            } ?: LoginResult.ErrorLoginResult("unknown")
        }

        setContent {
            App(loginResult)
        }
    }
}