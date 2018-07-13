package org.springframework.data.requery.kotlin.core

import io.requery.sql.KotlinEntityDataStore
import mu.KLogging
import org.springframework.data.requery.kotlin.mapping.RequeryMappingContext

/**
 * Java용 RequeryTemplate
 *
 * @author debop@coupang.com
 */
class RequeryTemplate(override val dataStore: KotlinEntityDataStore<Any>,
                      override val mappingContext: RequeryMappingContext): RequeryOperations {

    companion object: KLogging()

}