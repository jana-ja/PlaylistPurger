package de.janaja.playlistpurger

import androidx.compose.ui.window.ComposeUIViewController
import de.janaja.playlistpurger.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}