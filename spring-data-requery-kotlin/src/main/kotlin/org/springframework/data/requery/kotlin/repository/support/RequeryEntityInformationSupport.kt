package org.springframework.data.requery.kotlin.repository.support

import mu.KotlinLogging
import org.springframework.data.domain.Persistable
import org.springframework.data.repository.core.support.AbstractEntityInformation
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.repository.query.DefaultRequeryEntityMetadata
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * org.springframework.data.requery.repository.support.RequeryEntityInformationSupport
 *
 * @author debop
 */
abstract class RequeryEntityInformationSupport<E: Any, ID: Any>(override val kotlinType: KClass<E>)
    : AbstractEntityInformation<E, ID>(kotlinType.java), RequeryEntityInformation<E, ID> {

    companion object {
        private val log = KotlinLogging.logger { }

        @JvmStatic
        fun <E: Any, ID: Any> getEntityInformation(domainKlass: KClass<E>,
                                                   operations: RequeryOperations): RequeryEntityInformation<E, ID> {

            val entityModel = operations.entityModel

            log.debug { "domainClass=$domainKlass, entityModel=$entityModel" }


            //return if(Persistable::class.java.isAssignableFrom(kotlinType.java)) {
            return if(Persistable::class.isSuperclassOf(domainKlass)) {
                RequeryPersistableEntityInformation(domainKlass, entityModel)
            } else {
                RequeryEntityModelEntityInformation(domainKlass, entityModel)
            }
        }
    }

    private val metadata = DefaultRequeryEntityMetadata.of(kotlinType)

    override val entityName: String get() = metadata.entityName

    override val modelName: String get() = metadata.modelName

}