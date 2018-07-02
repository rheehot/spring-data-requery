package org.springframework.data.requery.cache

import io.requery.EntityCache
import io.requery.sql.EntityDataStore
import mu.KLogging
import org.cache2k.Cache
import org.cache2k.Cache2kBuilder
import org.cache2k.configuration.Cache2kConfiguration

/**
 * org.springframework.data.requery.cache.Cache2kEntityCache
 * @author debop
 * @since 18. 7. 2
 */
class Cache2kEntityCache @JvmOverloads constructor(
    val configuration: Cache2kConfiguration<Any, Any?> = DEFAULT_CONFIGURATION
): EntityCache {

    companion object: KLogging() {

        private val DEFAULT_CONFIGURATION: Cache2kConfiguration<Any, Any?> = Cache2kConfiguration.of(Any::class.java, Any::class.java)
            .apply {
                entryCapacity = 20000L
                isEternal = true
                isKeepDataAfterExpired = false
                isBoostConcurrency = true
                retryInterval = 10
                maxRetryInterval = 1000L
            }

        private val cacheManager: MutableMap<Class<*>, Cache<Any, Any?>> = LinkedHashMap()
        private val syncObj: Any = Any()
    }

    var dataStore: EntityDataStore<Any>? = null

    val Class<*>.cache: Cache<Any, Any?>
        get() {
            synchronized(syncObj) {
                var cache = cacheManager[this]

                if(cache == null) {
                    logger.debug { "Create Cache2k cache for type [${this.name}]" }

                    val cacheBuilder = Cache2kBuilder.of(configuration)
                        .name(this.name)

                    dataStore?.let {
                        cacheBuilder.loader { key ->
                            dataStore?.findByKey(this, key)
                        }
                    }

                    cache = cacheBuilder.build()
                    cacheManager[this] = cache
                }
                return cache!!
            }
        }

    override fun contains(type: Class<*>?, key: Any?): Boolean {
        return type?.cache?.containsKey(key) ?: false
    }

    override fun invalidate(type: Class<*>?) {
        type?.cache?.removeAll()
    }

    override fun invalidate(type: Class<*>?, key: Any?) {
        key?.let { type?.cache?.remove(key) }
    }

    override fun clear() {
        synchronized(syncObj) {
            cacheManager.forEach { _, cache ->
                cache.clear()
            }
        }
    }

    override fun <T: Any?> put(type: Class<T>?, key: Any?, value: T) {
        value?.let {
            key?.let { type?.cache?.put(key, value) }
        } ?: invalidate(type, key)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T: Any?> get(type: Class<T>?, key: Any?): T {
        return key?.let {
            type?.cast(type.cache.get(key))
        } ?: null as T
    }

}