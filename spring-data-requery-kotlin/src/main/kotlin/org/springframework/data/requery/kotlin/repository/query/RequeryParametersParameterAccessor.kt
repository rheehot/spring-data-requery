package org.springframework.data.requery.repository.query

import org.springframework.data.repository.query.ParametersParameterAccessor

/**
 * This class provides access to parameters of a user-defined queryMethod. It wraps ParametersParameterAccessor which catches
 * special parameters Sort and Pageable.
 *
 * @author debop@coupang.com
 */
class RequeryParametersParameterAccessor(override val parameters: RequeryParameters,
                                         values: Array<Any?>)
    : ParametersParameterAccessor(parameters, values), RequeryParameterAccessor {

    constructor(method: RequeryQueryMethod, values: Array<Any?>): this(method.parameters, values)

}