package com.coupang.springframework.data.requery.core

import io.requery.TransactionIsolation
import io.requery.sql.KotlinEntityDataStore

/**
 * Kotlin 용 RequeryTemplate
 *
 * @property dataStore Kotlin용 EntityDataStore
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
class KotlinRequeryTemplate(override val dataStore: KotlinEntityDataStore<Any>): KotlinRequeryOperations {

    override fun <T: Any> withTransaction(block: KotlinRequeryOperations.() -> T): T {
        return dataStore.withTransaction { block.invoke(this@KotlinRequeryTemplate) }
    }

    override fun <T: Any> withTransaction(isolation: TransactionIsolation, block: KotlinRequeryOperations.() -> T): T {
        return dataStore.withTransaction(isolation) { block.invoke(this@KotlinRequeryTemplate) }
    }

    override fun <T: Any> withDataStore(block: KotlinEntityDataStore<Any>.() -> T): T {
        return block.invoke(dataStore)
    }
}