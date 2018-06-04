package com.coupang.springframework.data.requery.repository.support

import com.coupang.springframework.data.requery.mapping.RequeryMappingContext
import io.requery.query.FieldExpression
import org.springframework.data.domain.Example

/**
 * RequeryExampleConverter
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryExampleConverter<T>(private val context: RequeryMappingContext) {

    fun convertExampleToPredicate(example: Example<T>): List<FieldExpression<T>> {
        TODO("Not Implemented")
    }

}