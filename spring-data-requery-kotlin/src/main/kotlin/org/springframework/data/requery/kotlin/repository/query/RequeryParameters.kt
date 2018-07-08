package org.springframework.data.requery.kotlin.repository.query

import mu.KotlinLogging
import org.springframework.core.MethodParameter
import org.springframework.data.repository.query.Parameter
import org.springframework.data.repository.query.Parameters
import java.lang.reflect.Method
import java.util.*

/**
 * 메소드의 Parameter 정보를 나타냅니다.
 *
 * @author debop
 */
class RequeryParameters: Parameters<RequeryParameters, RequeryParameter> {

    constructor(method: Method): super(method)
    constructor(parameters: List<RequeryParameter>): super(parameters)

    private val log = KotlinLogging.logger { }

    override fun createParameter(parameter: MethodParameter): RequeryParameter =
        RequeryParameter(parameter)

    override fun createFrom(parameters: MutableList<RequeryParameter>): RequeryParameters =
        RequeryParameters(parameters)

    override fun toString(): String {
        return bindableParameters.joinToString(",") { it.toString() }
    }
}

class RequeryParameter(val parameter: MethodParameter): Parameter(parameter) {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    init {
        log.debug { "Create RequeryParameter. parameter=$parameter" }
    }

    val isDateParameter: Boolean
        get() = Objects.equals(type, Date::class.java)

    override fun toString(): String = parameter.parameter.toString()
}