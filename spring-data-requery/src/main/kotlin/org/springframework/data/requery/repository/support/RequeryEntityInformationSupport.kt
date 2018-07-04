package org.springframework.data.requery.repository.support

import mu.KotlinLogging
import org.springframework.data.domain.Persistable
import org.springframework.data.repository.core.support.AbstractEntityInformation
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.repository.query.DefaultRequeryEntityMetadata

/**
 * org.springframework.data.requery.repository.support.RequeryEntityInformationSupport
 *
 * @author debop
 */
abstract class RequeryEntityInformationSupport<E: Any, ID: Any>(domainClass: Class<E>)
    : AbstractEntityInformation<E, ID>(domainClass), RequeryEntityInformation<E, ID> {

    companion object {
        private val log = KotlinLogging.logger { }

        @JvmStatic
        fun <E: Persistable<ID>, ID: Any> getEntityInformation(domainClass: Class<E>,
                                                               operations: RequeryOperations): RequeryEntityInformation<E, ID> {

            val entityModel = operations.entityModel

            log.debug { "domainClass=$domainClass, entityModel=$entityModel" }

            return if(Persistable::class.java.isAssignableFrom(domainClass)) {
                RequeryPersistableEntityInformation(domainClass, entityModel)
            } else {
                RequeryEntityModelEntityInformation(domainClass, entityModel)
            }
        }
    }

    private val metadata = DefaultRequeryEntityMetadata.of(domainClass)

    override val entityName: String get() = metadata.entityName

    override val modelName: String get() = metadata.modelName

}