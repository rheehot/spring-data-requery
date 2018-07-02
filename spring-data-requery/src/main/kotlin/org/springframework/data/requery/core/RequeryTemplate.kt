package org.springframework.data.requery.core

import io.requery.sql.EntityDataStore
import org.springframework.context.ApplicationContext
import org.springframework.data.requery.mapping.RequeryMappingContext

/**
 * RequeryTemplate
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
class RequeryTemplate(val applicationContext: ApplicationContext,
                      val dataStore: EntityDataStore<Any>,
                      val mappingContext: RequeryMappingContext) {
}