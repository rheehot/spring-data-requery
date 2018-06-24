package com.coupang.springframework.data.requery.core

import com.coupang.kotlinx.logging.KLogging
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

    companion object: KLogging()

    override fun <T: Any> withTransaction(isolation: TransactionIsolation?, block: KotlinRequeryOperations.() -> T): T {
        return isolation?.let {
            dataStore.withTransaction(it) { block.invoke(this@KotlinRequeryTemplate) }
        } ?: dataStore.withTransaction { block.invoke(this@KotlinRequeryTemplate) }
    }
}