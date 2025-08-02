package de.janaja.playlistpurger

import androidx.compose.ui.window.ComposeUIViewController
import de.janaja.playlistpurger.core.ui.AppEntry
import de.janaja.playlistpurger.core.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    AppEntry()
}