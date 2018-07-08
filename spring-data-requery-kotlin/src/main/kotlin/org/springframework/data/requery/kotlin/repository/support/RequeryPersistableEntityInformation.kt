package org.springframework.data.requery.kotlin.repository.support

import io.requery.meta.EntityModel
import mu.KotlinLogging
import org.springframework.data.domain.Persistable
import kotlin.reflect.KClass

/**
 * org.springframework.data.requery.repository.support.RequeryPersistableEntityInformation
 *
 * @author debop
 */
class RequeryPersistableEntityInformation<E: Persistable<ID>, ID: Any>(kotlinType: KClass<E>,
                                                                       entityModel: EntityModel)
    : RequeryEntityModelEntityInformation<E, ID>(kotlinType, entityModel) {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    init {
        log.debug { "Create RequeryPersistableEntityInformation. kotlinType=$kotlinType, entityModel=${entityModel.name}" }
    }

    override fun isNew(entity: E): Boolean {
        log.trace { "Is new entity?. entity=$entity" }
        return entity.isNew
    }

    override fun getId(entity: E): ID? {
        log.trace { "Get identifier of entity. entity=$entity" }
        return entity.id
    }

}