package org.springframework.data.requery.kotlin.core

import io.requery.sql.EntityDataStore
import mu.KLogging
import org.springframework.context.ApplicationContext

/**
 * Java용 RequeryTemplate
 *
 * @author debop@coupang.com
 */
class RequeryTemplate(override val applicationContext: ApplicationContext,
                      override val dataStore: EntityDataStore<Any>,
                      override val mappingContext: RequeryMappingContext): RequeryOperations {

    companion object: KLogging()

}