package com.coupang.springframework.data.requery.core.coroutines

import com.coupang.kotlinx.logging.KLogging
import io.requery.TransactionIsolation
import io.requery.sql.KotlinEntityDataStore

/**
 * com.coupang.springframework.data.requery.core.coroutines.CoroutineRequeryTemplate
 * @author debop
 * @since 18. 6. 2
 */
class CoroutineRequeryTemplate(override val dataStore: KotlinEntityDataStore<Any>): CoroutineRequeryOperations {

    companion object: KLogging()

    override suspend fun <T: Any> withTransaction(isolation: TransactionIsolation?, block: CoroutineRequeryOperations.() -> T): T {
        return isolation?.let {
            dataStore.withTransaction(isolation!!) {
                block.invoke(this@CoroutineRequeryTemplate)
            }
        } ?: dataStore.withTransaction { block.invoke(this@CoroutineRequeryTemplate) }

    }

}