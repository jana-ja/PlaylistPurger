package de.janaja.playlistpurger.core.util

expect class ConcurrentLruCache<K : Any, V : Any>(maxCount: Int) : MutableMap<K, V>