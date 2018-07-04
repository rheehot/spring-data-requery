package org.springframework.data.requery.provider

import mu.KotlinLogging
import org.springframework.data.requery.core.RequeryOperations

/**
 * org.springframework.data.requery.provider.RequeryPersistenceProvider
 *
 * @author debop
 */
@Deprecated("사용할 필요가 없는 듯 ...")
class RequeryPersistenceProvider(val operations: RequeryOperations): ProxyIdAccessor {

    companion object {
        private val log = KotlinLogging.logger { }

        @JvmStatic
        fun of(operations: RequeryOperations): RequeryPersistenceProvider = RequeryPersistenceProvider(operations)
    }

    override fun shouldUseAccessorFor(entity: Any?): Boolean = false

    override fun getIdentifierFrom(entity: Any?): Any? = null

    fun <E> potentiallyConvertEmptyCollection(collection: Collection<E>?): Collection<E>? {
        return collection?.let {
            when {
                it.isNotEmpty() -> it
                else -> null
            }
        }
    }
}