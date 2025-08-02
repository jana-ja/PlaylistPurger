package de.janaja.playlistpurger.features.settings.domain

data class Settings(
    val showSwipeFirst: Boolean = true // TODO how to handle default values? also for pref key in datastore, should be somewhere in domain
)

