package com.coupang.springframework.data.requery

import io.requery.sql.EntityDataStore

/**
 * RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryOperations {

    val dataStore: EntityDataStore<Any>

    fun <T, K> findById(clazz: Class<T>, id: K): T? = dataStore.findByKey<T, K>(clazz, id)

    @JvmDefault
    fun <T> delete(entity: T) {
        dataStore.delete(entity)
    }
}