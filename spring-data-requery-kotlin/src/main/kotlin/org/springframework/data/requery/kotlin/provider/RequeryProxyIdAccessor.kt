package org.springframework.data.requery.kotlin.provider

import io.requery.sql.KotlinEntityDataStore
import mu.KotlinLogging
import org.springframework.data.domain.Persistable
import org.springframework.data.requery.kotlin.getEntityModel

/**
 * org.springframework.data.requery.provider.RequeryProxyIdAccessor
 *
 * @author debop
 */
@Deprecated("사용할 필요 없다")
class RequeryProxyIdAccessor(dataStore: KotlinEntityDataStore<Any>): ProxyIdAccessor {

    private val entityModel = dataStore.getEntityModel()
    private val log = KotlinLogging.logger { }

    override fun shouldUseAccessorFor(entity: Any?): Boolean {

        log.trace { "Check entity is requery entity?. entity=$entity" }

        return entity?.let { entityModel.containsTypeOf(entity.javaClass) } ?: false
    }

    override fun getIdentifierFrom(entity: Any?): Any? {

        log.trace { "Get identifier value of entity. entity=$entity" }

        return entity?.let {
            when(it) {
                is Persistable<*> -> it.id
                else -> null
            }
        }
    }
}