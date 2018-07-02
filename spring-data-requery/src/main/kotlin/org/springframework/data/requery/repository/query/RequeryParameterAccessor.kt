package org.springframework.data.requery.repository.query

import org.springframework.data.repository.query.ParameterAccessor

/**
 * org.springframework.data.requery.repository.query.RequeryParameterAccessor
 *
 * @author debop
 */

interface RequeryParameterAccessor: ParameterAccessor {

    val parameters: RequeryParameters

}