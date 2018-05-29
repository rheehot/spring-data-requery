package com.coupang.springframework.data.requery.repository.query

import com.coupang.kotlinx.logging.KLogging
import org.springframework.core.MethodParameter
import org.springframework.data.repository.query.Parameter
import org.springframework.data.repository.query.Parameters
import java.lang.reflect.Method

/**
 * RequeryParameters
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryParameters(private val method: Method)
    : Parameters<RequeryParameters, RequeryParameters.RequeryParameter>(method) {

    companion object: KLogging()

    override fun createFrom(parameters: MutableList<RequeryParameter>?): RequeryParameters {
        TODO("not implemented")
    }

    override fun createParameter(parameter: MethodParameter?): RequeryParameter {
        TODO("not implemented")
    }

    class RequeryParameter(private val parameter: MethodParameter): Parameter(parameter) {

    }
}