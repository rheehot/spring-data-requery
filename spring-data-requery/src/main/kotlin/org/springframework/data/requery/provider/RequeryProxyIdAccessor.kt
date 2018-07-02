package org.springframework.data.requery.provider

import io.requery.sql.EntityDataStore
import mu.KotlinLogging
import org.springframework.data.domain.Persistable
import org.springframework.data.requery.getEntityModel

/**
 * org.springframework.data.requery.provider.RequeryProxyIdAccessor
 *
 * @author debop
 */
@Deprecated("사용할 필요 없다")
class RequeryProxyIdAccessor(dataStore: EntityDataStore<Any>): ProxyIdAccessor {

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