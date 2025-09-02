package de.janaja.playlistpurger.core.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object MenuRoute

@Serializable
object PlaylistOverviewRoute

@Serializable
object SettingsRoute

@Serializable
data class TrackListRoute(
    val playlistId: String,
    val playlistName: String
)

@Serializable
data class VoteResultRoute(
    val playlistId: String,
    val playlistName: String
)