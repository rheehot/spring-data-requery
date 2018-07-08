package org.springframework.data.requery.kotlin.mapping

import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.data.mapping.context.AbstractMappingContext
import org.springframework.data.mapping.model.FieldNamingStrategy
import org.springframework.data.mapping.model.Property
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy
import org.springframework.data.mapping.model.SimpleTypeHolder
import org.springframework.data.util.TypeInformation

/**
 * RequeryMappingContext
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
open class RequeryMappingContext: AbstractMappingContext<DefaultRequeryPersistentEntity<*>, RequeryPersistentProperty>(),
                                  ApplicationContextAware {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    private var fieldNamingStrategy: FieldNamingStrategy? = PropertyNameFieldNamingStrategy.INSTANCE
    private var applicationContext: ApplicationContext? = null

    open fun setFieldNamingStrategy(fieldNamingStrategy: FieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy
    }

    open override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    open override fun <T> createPersistentEntity(typeInformation: TypeInformation<T>): DefaultRequeryPersistentEntity<*> {

        return DefaultRequeryPersistentEntity(typeInformation)
            .apply {
                log.debug { "Create persistent entity. typeInformation=$typeInformation" }
                applicationContext?.let { this.setApplicationContext(it) }
            }
    }

    open override fun createPersistentProperty(property: Property,
                                               owner: DefaultRequeryPersistentEntity<*>,
                                               simpleTypeHolder: SimpleTypeHolder): RequeryPersistentProperty {
        log.debug { "Create property. property=$property" }

        return DefaultRequeryPersistentProperty(property,
                                                owner,
                                                simpleTypeHolder,
                                                fieldNamingStrategy ?: PropertyNameFieldNamingStrategy.INSTANCE)
    }
}
