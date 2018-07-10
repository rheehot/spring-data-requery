package org.springframework.data.requery.kotlin.coroutines

import io.requery.TransactionIsolation
import io.requery.sql.KotlinEntityDataStore
import mu.KotlinLogging

/**
 * [KotlinCoroutineRequeryOperations] 의 구현체 입니다.
 *
 * @author debop
 * @since 18. 6. 2
 */
class KotlinCoroutineRequeryTemplate(override val dataStore: KotlinEntityDataStore<Any>): KotlinCoroutineRequeryOperations {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    override suspend fun <T: Any> withTransaction(isolation: TransactionIsolation?, block: KotlinCoroutineRequeryOperations.() -> T): T {
        return isolation?.let {
            dataStore.withTransaction(isolation) {
                block.invoke(this@KotlinCoroutineRequeryTemplate)
            }
        } ?: dataStore.withTransaction { block.invoke(this@KotlinCoroutineRequeryTemplate) }

    }
}