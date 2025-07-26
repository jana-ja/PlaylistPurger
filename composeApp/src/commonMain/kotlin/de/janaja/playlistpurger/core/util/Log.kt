package de.janaja.playlistpurger.core.util

expect class Log {
    companion object {
        fun d(tag: String, msg: String)
        fun i(tag: String, msg: String)
        fun e(tag: String, msg: String)
        fun e(tag: String, msg: String, e: Throwable)
    }
}