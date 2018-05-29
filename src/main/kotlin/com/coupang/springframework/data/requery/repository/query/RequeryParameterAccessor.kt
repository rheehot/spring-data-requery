package com.coupang.springframework.data.requery.repository.query

import org.springframework.data.repository.query.ParameterAccessor

/**
 * RequeryParameterAccessor
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryParameterAccessor: ParameterAccessor {

    fun getParameters(): RequeryParameters

    // fun getQueryOptions(): RequeryQueryOptions

    fun getBindVars(): Map<String, Any?>
}