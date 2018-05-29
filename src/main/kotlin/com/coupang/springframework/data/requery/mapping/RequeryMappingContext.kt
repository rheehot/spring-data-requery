package com.coupang.springframework.data.requery.mapping

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.data.mapping.context.AbstractMappingContext
import org.springframework.data.mapping.model.FieldNamingStrategy
import org.springframework.data.mapping.model.Property
import org.springframework.data.mapping.model.SimpleTypeHolder
import org.springframework.data.util.TypeInformation

/**
 * RequeryMappingContext
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryMappingContext
    : AbstractMappingContext<DefaultRequeryPersistentEntity<*>, RequeryPersistentProperty>(),
      ApplicationContextAware {

    private var fieldNamingStrategy: FieldNamingStrategy? = null
    private var applicationContext: ApplicationContext? = null

    open override fun <T: Any?> createPersistentEntity(typeInformation: TypeInformation<T>): DefaultRequeryPersistentEntity<*> {
        TODO("not implemented")
    }

    open override fun createPersistentProperty(property: Property,
                                               owner: DefaultRequeryPersistentEntity<*>,
                                               simpleTypeHolder: SimpleTypeHolder): RequeryPersistentProperty {
        TODO("not implemented")
    }

    open override fun setApplicationContext(applicationContext: ApplicationContext) {
        TODO("not implemented")
    }

}