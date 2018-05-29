package com.coupang.springframework.data.requery.repository.query

import org.springframework.data.repository.query.ParametersParameterAccessor

/**
 * RequeryParametersParameterAccessor
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
class RequeryParametersParameterAccessor(private val method: RequeryQueryMethod,
                                         private val values: Array<Any?>)
    : ParametersParameterAccessor(method.parameters, values), RequeryParameterAccessor {
}