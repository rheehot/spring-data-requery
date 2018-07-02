package org.springframework.data.requery.mapping

import mu.KLogging
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.data.mapping.context.AbstractMappingContext
import org.springframework.data.mapping.model.FieldNamingStrategy
import org.springframework.data.mapping.model.Property
import org.springframework.data.mapping.model.SimpleTypeHolder
import org.springframework.data.util.TypeInformation
import java.util.*

/**
 * RequeryMappingContext
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
class RequeryMappingContext: AbstractMappingContext<DefaultRequeryPersistentEntity<*>, RequeryPersistentProperty>(),
                             ApplicationContextAware {

    companion object: KLogging()

    private var fieldNamingStrategy: FieldNamingStrategy? = null
    private var applicationContext: Optional<ApplicationContext>? = null

    init {
        applicationContext = Optional.empty()
    }

    fun setFieldNamingStrategy(fieldNamingStrategy: FieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = Optional.of(applicationContext)
    }

    override fun <T> createPersistentEntity(typeInformation: TypeInformation<T>): DefaultRequeryPersistentEntity<*> {
        val entity = DefaultRequeryPersistentEntity(typeInformation)
        applicationContext!!.ifPresent(Consumer<ApplicationContext> { entity.setApplicationContext() })
        logger.debug("Create persistent entity. typeInformation={}", typeInformation)
        return entity
    }

    override fun createPersistentProperty(property: Property,
                                          owner: DefaultRequeryPersistentEntity<*>,
                                          simpleTypeHolder: SimpleTypeHolder): RequeryPersistentProperty {
        logger.debug("Create property. property={}", property)
        return DefaultRequeryPersistentProperty(property,
                                                owner,
                                                simpleTypeHolder,
                                                fieldNamingStrategy)
    }
}
