package de.janaja.playlistpurger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.janaja.playlistpurger.util.LoginResponseHelper
import org.koin.mp.KoinPlatform.getKoin


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // when activity is started from spotify login website -> intent contains response data
        intent.data?.let {
            val loginResponseHelper: LoginResponseHelper = getKoin().get()
            loginResponseHelper.handleResponseUrl(it.toString())
        }

        setContent {
            App()
        }
    }
}