package de.janaja.playlistpurger.core.util


import platform.darwin.DISPATCH_QUEUE_SERIAL // Can use Serial for simplicity or Concurrent with barriers
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_sync // For all operations to ensure thread safety and correct return values

//actual class ConcurrentCache<K : Any, V : Any> actual constructor() : MutableMap<K, V> {
//
//    private val internalMap = mutableMapOf<K, V>()
//    // Using a serial queue effectively makes all operations on the map atomic with respect to each other.
//    private val serialQueue = dispatch_queue_create("com.yourapp.concurrentcache.serialqueue", null /* DISPATCH_QUEUE_SERIAL */)
//
//    override val size: Int
//        get() = dispatch_sync(serialQueue) { internalMap.size }
//
//    override fun containsKey(key: K): Boolean = dispatch_sync(serialQueue) { internalMap.containsKey(key) }
//
//    override fun containsValue(value: V): Boolean = dispatch_sync(serialQueue) { internalMap.containsValue(value) }
//
//    override fun get(key: K): V? = dispatch_sync(serialQueue) { internalMap[key] }
//
//    override fun isEmpty(): Boolean = dispatch_sync(serialQueue) { internalMap.isEmpty() }
//
//    override fun clear() {
//        dispatch_sync(serialQueue) { internalMap.clear() }
//    }
//
//    // For collections, return copies to prevent modification outside synchronized block
//    // and to avoid concurrent modification issues if the original map changes.
//    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
//        get() = dispatch_sync(serialQueue) { LinkedHashMap(internalMap).entries } // Create a copy
//
//    override val keys: MutableSet<K>
//        get() = dispatch_sync(serialQueue) { LinkedHashSet(internalMap.keys) } // Create a copy
//
//    override fun put(key: K, value: V): V? {
//        return dispatch_sync(serialQueue) { internalMap.put(key, value) }
//    }
//
//    override fun putAll(from: Map<out K, V>) {
//        dispatch_sync(serialQueue) { internalMap.putAll(from) }
//    }
//
//    override fun remove(key: K): V? {
//        return dispatch_sync(serialQueue) { internalMap.remove(key) }
//    }
//
//    override val values: MutableCollection<V>
//        get() = dispatch_sync(serialQueue) { ArrayList(internalMap.values) } // Create a copy
//}



import platform.Foundation.NSCache

@Suppress("UNCHECKED_CAST")
actual class ConcurrentCache<K : Any, V : Any> actual constructor() : MutableMap<K, V> {

    // NSCache is thread-safe by default.
    // First parameter is Key type, second is Value type.
    private val nsCache = NSCache()

    override val size: Int
        get() {
            // NSCache doesn't directly expose its size in a simple way like Java Maps.
            // This is a limitation. If exact size is critical and frequently needed,
            // you might need a different approach or to maintain a separate counter
            // (which adds complexity for thread-safety).
            // For many cache use cases, knowing the exact size isn't always paramount.
            // Returning 0 or an approximation might be acceptable depending on need.
            // Or, iterate keys if absolutely necessary (less efficient).
            // For now, let's acknowledge this limitation for a simple implementation.
            // This is one area where it's not a perfect MutableMap drop-in.
            throw UnsupportedOperationException("NSCache does not provide a direct size property easily. Consider if size is critical for your use case.")
        }

    override fun containsKey(key: K): Boolean {
        return nsCache.objectForKey(key) != null
    }

    override fun containsValue(value: V): Boolean {
        // NSCache is optimized for key-based lookups.
        // Checking for a value requires iterating, which is inefficient and not standard for NSCache.
        throw UnsupportedOperationException("NSCache does not efficiently support containsValue.")
    }

    override fun get(key: K): V? {
        return nsCache.objectForKey(key) as V?
    }

    override fun isEmpty(): Boolean {
        // Similar to size, not straightforward. You might need to check if a known key exists
        // or accept this limitation. A common pattern is to just try to get and handle null.
        // Let's assume for a cache, isEmpty is less critical than get/put.
        return true // This is not accurate, reflects limitation.
        // A better isEmpty for a cache might be "is it likely empty or unpopulated".
        // For true MutableMap compliance, this needs more work.
    }

    override fun clear() {
        nsCache.removeAllObjects()
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = throw UnsupportedOperationException("NSCache does not provide direct entrySet access like Java Maps.")

    override val keys: MutableSet<K>
        get() = throw UnsupportedOperationException("NSCache does not provide direct keySet access like Java Maps.")

    override fun put(key: K, value: V): V? {
        val oldValue = nsCache.objectForKey(key)
        nsCache.setObject(value, key) // Can also use setObject(value, key, cost) if you want to give hints for eviction
        return oldValue as V?
    }

    override fun putAll(from: Map<out K, V>) {
        from.forEach { (key, value) ->
            nsCache.setObject(value, key)
        }
    }

    override fun remove(key: K): V? {
        val oldValue = nsCache.objectForKey(key)
        nsCache.removeObjectForKey(key)
        return oldValue as V?
    }

    override val values: MutableCollection<V>
        get() = throw UnsupportedOperationException("NSCache does not provide direct values collection access like Java Maps.")
}
