package de.janaja.playlistpurger

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform