package de.janaja.playlistpurger.core.util



import platform.Foundation.NSCache

@Suppress("UNCHECKED_CAST")
actual class ConcurrentLruCache<K : Any, V : Any> actual constructor(private val maxCount: Int) : MutableMap<K, V> {

    private val nsCache = NSCache().apply {
        // When the limit is reached, NSCache may discard objects to make space,
        // though the exact eviction order isn't strictly LRU but is based on its own heuristics
        // (often considering cost, if provided, and general memory pressure).
        countLimit = maxCount.toULong() // NSCache.countLimit is NSUInteger

        // Optional: You could also use totalCostLimit if your cached objects have a "cost".
        // To use totalCostLimit, you also need to call setObject:forKey:cost: when adding items.
        // nsCache.totalCostLimit = someTotalCostInBytes.toULong()
    }

    // MutableMap Interface Implementation
    // operations like size, isEmpty, entries, keys, values are not efficiently supported by NSCache.

    override val size: Int
        get() {
            // NSCache doesn't directly expose its size efficiently.
            // For a size-limited cache, knowing the exact current size might be less critical
            // than knowing it won't exceed the 'countLimit'.
            // Returning what's implied by countLimit might be misleading as it's an upper bound.
            // If you absolutely need a current count, you'd have to maintain it separately
            // (adding complexity and synchronization needs) or iterate, which is inefficient.
            // Given we set a countLimit, this becomes less of an issue for "not getting too big".
            throw UnsupportedOperationException("NSCache does not provide a direct size property easily. Cache is bounded by countLimit = $maxCount.")
        }

    override fun containsKey(key: K): Boolean {
        return nsCache.objectForKey(key) != null
    }

    override fun containsValue(value: V): Boolean {
        // inefficient for NSCache.
        throw UnsupportedOperationException("NSCache does not efficiently support containsValue.")
    }

    override fun get(key: K): V? {
        return nsCache.objectForKey(key) as V?
    }

    override fun isEmpty(): Boolean {
        // Approximating isEmpty based on whether any known key returns a value is possible
        // but not perfectly accurate. For a cache, often you just try to 'get' and handle null.
        // Given we have countLimit, the concern about "is it empty vs full" is managed.
        // A simple (but not always correct) check:
        // return nsCache.allKeys.count == 0 (don't use allKeys often, it can be slow)
        // For simplicity, let's keep it unsupported or return a best guess.
        throw UnsupportedOperationException("isEmpty is not efficiently determined for NSCache. Cache is bounded by countLimit = $maxCount.")
    }

    override fun clear() {
        nsCache.removeAllObjects()
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = throw UnsupportedOperationException("NSCache does not provide direct entrySet access.")

    override val keys: MutableSet<K>
        get() = throw UnsupportedOperationException("NSCache does not provide direct keySet access.")

    override fun put(key: K, value: V): V? {
        val oldValue = nsCache.objectForKey(key) as V?

        // If using totalCostLimit, you would use:
        // nsCache.setObject(value, key, costOfValue)
        nsCache.setObject(value, key)
        return oldValue
    }

    override fun putAll(from: Map<out K, V>) {
        // Be mindful if 'from' is very large, as each 'put' is an individual operation.
        // If 'from.size' could exceed 'countLimit', many items might be added and immediately
        // become candidates for eviction.
        from.forEach { (key, value) ->
            put(key, value)
        }
    }

    override fun remove(key: K): V? {
        val oldValue = nsCache.objectForKey(key) as V?
        nsCache.removeObjectForKey(key)
        return oldValue
    }

    override val values: MutableCollection<V>
        get() = throw UnsupportedOperationException("NSCache does not provide direct values collection access.")
}

