package de.janaja.playlistpurger.core.util


actual class Log {
    actual companion object {
        actual fun d(tag: String, msg: String) {
            android.util.Log.d(tag, msg)
        }

        actual fun i(tag: String, msg: String) {
            android.util.Log.i(tag, msg)
        }

        actual fun e(tag: String, msg: String) {
            android.util.Log.e(tag, msg)
        }

        actual fun e(tag: String, msg: String, e: Throwable) {
            android.util.Log.e(tag, msg, e)
        }
    }
}