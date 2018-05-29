package com.coupang.springframework.data.requery.mapping

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.data.mapping.context.AbstractMappingContext
import org.springframework.data.mapping.model.FieldNamingStrategy

/**
 * RequeryMappingContext
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryMappingContext
    : AbstractMappingContext<DefaultRequeryPersistentEntity<*>, RequeryPersistentProperty>,
      ApplicationContextAware {

    private var fieldNamingStrategy: FieldNamingStrategy? = null
    private var applicationContext: ApplicationContext? = null
}