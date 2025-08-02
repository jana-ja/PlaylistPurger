package de.janaja.playlistpurger.core.util

actual class ConcurrentLruCache<K : Any, V : Any> actual constructor(private val maxCount: Int) :
    MutableMap<K, V> {

    private val lruDelegate: MutableMap<K, V> =
        object : LinkedHashMap<K, V>(maxCount, 0.75f, true) { // true for access-order
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>): Boolean {
                return size > maxCount
            }
        }

    // LinkedHashMap is not thread safe. Must be synchronized.
    private val synchronizedLruMap: MutableMap<K, V> =
        java.util.Collections.synchronizedMap(lruDelegate)

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = synchronizedLruMap.entries

    override val keys: MutableSet<K>
        get() = synchronizedLruMap.keys

    // Delegate all MutableMap methods to synchronizedLruMap
    override val size: Int get() = synchronizedLruMap.size

    override val values: MutableCollection<V>
        get() = synchronizedLruMap.values

    override fun clear() {
        synchronizedLruMap.clear()
    }

    override fun containsValue(value: V): Boolean =
        synchronizedLruMap.containsValue(value)

    override fun isEmpty(): Boolean =
        synchronizedLruMap.isEmpty()

    override fun remove(key: K): V? =
        synchronizedLruMap.remove(key)

    override fun putAll(from: Map<out K, V>) {
        synchronizedLruMap.putAll(from)
    }

    override fun containsKey(key: K): Boolean = synchronizedLruMap.containsKey(key)

    override fun get(key: K): V? = synchronizedLruMap[key]

    override fun put(key: K, value: V): V? = synchronizedLruMap.put(key, value)
}