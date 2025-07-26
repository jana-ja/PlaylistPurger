package de.janaja.playlistpurger.core.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ptr
import platform.darwin.OS_LOG_DEFAULT
import platform.darwin.OS_LOG_TYPE_DEBUG
import platform.darwin.OS_LOG_TYPE_ERROR
import platform.darwin.OS_LOG_TYPE_INFO
import platform.darwin.__dso_handle
import platform.darwin._os_log_internal


actual class Log {
    actual companion object {
        @OptIn(ExperimentalForeignApi::class)
        actual fun d(tag: String, msg: String) {
            _os_log_internal(
                __dso_handle.ptr,
                OS_LOG_DEFAULT,
                OS_LOG_TYPE_DEBUG,
                "D $tag $msg"
            )
        }

        @OptIn(ExperimentalForeignApi::class)
        actual fun i(tag: String, msg: String) {
            _os_log_internal(
                __dso_handle.ptr,
                OS_LOG_DEFAULT,
                OS_LOG_TYPE_INFO,
                "I $tag $msg"
            )
        }

        @OptIn(ExperimentalForeignApi::class)
        actual fun e(tag: String, msg: String) {
            _os_log_internal(
                __dso_handle.ptr,
                OS_LOG_DEFAULT,
                OS_LOG_TYPE_ERROR,
                "E $tag $msg"
            )
        }

        @OptIn(ExperimentalForeignApi::class)
        actual fun e(tag: String, msg: String, e: Throwable) {
            _os_log_internal(
                __dso_handle.ptr,
                OS_LOG_DEFAULT,
                OS_LOG_TYPE_ERROR,
                "E $tag $msg $e"
            )
        }
    }
}