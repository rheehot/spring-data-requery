package org.springframework.data.requery.repository.query

import io.requery.query.Expression
import io.requery.query.FieldExpression
import io.requery.query.NamedExpression
import mu.KotlinLogging
import org.springframework.data.repository.query.Parameter
import org.springframework.data.repository.query.Parameters
import org.springframework.data.repository.query.ParametersParameterAccessor
import org.springframework.data.repository.query.parser.Part
import org.springframework.data.requery.namedExpressionOf
import org.springframework.data.requery.provider.RequeryPersistenceProvider
import org.springframework.util.ClassUtils
import org.springframework.util.ObjectUtils
import java.util.*

/**
 * Helper class to allow easy creation of {@link ParameterMetadata}s.
 *
 * @author debop@coupang.com
 */
class ParameterMetadataProvider @JvmOverloads constructor(val provider: RequeryPersistenceProvider,
                                                          parameters: Parameters<*, *>,
                                                          val bindableParameterValues: Iterator<Any?>? = null) {

    constructor(provider: RequeryPersistenceProvider, accessor: ParametersParameterAccessor)
        : this(provider, accessor.parameters, accessor.iterator())

    companion object {
        private val log = KotlinLogging.logger { }
    }

    private val expressions: MutableList<ParameterMetadata<out Any>> = arrayListOf()
    private val parameters: Iterator<Parameter> = parameters.getBindableParameters().iterator()


    fun getExpressions(): List<ParameterMetadata<out Any>> = expressions.toList()

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> next(part: Part): ParameterMetadata<T> {

        require(parameters.hasNext()) { "No parameter available for part. part=$part" }

        val parameter = parameters.next()
        return next(part, parameter.type as Class<T>, parameter)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> next(part: Part, type: Class<T>): ParameterMetadata<T> {

        val parameter = parameters.next()
        val typeToUse: Class<*> = if(ClassUtils.isAssignable(type, parameter.type)) parameter.type else type
        return next(part, typeToUse as Class<T>, parameter)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T: Any> next(part: Part, type: Class<T>, parameter: Parameter): ParameterMetadata<T> {

        log.debug { "Get next parameter ... part=$part, type=$type, parameter=$parameter" }

        val reifiedType: Class<T> = if(Expression::class.java == type) Any::class.java as Class<T> else type

        val nameGetter = { parameter.name.orElseThrow { IllegalArgumentException("Parameter needs to be named") } }

        val expression: NamedExpression<T> = when {
            parameter.isExplicitlyNamed -> namedExpressionOf(nameGetter.invoke(), reifiedType)
            else -> namedExpressionOf(parameter.index.toString(), reifiedType)
        }

        val value = when(bindableParameterValues) {
            null -> ParameterMetadata.PLACEHOLDER
            else -> bindableParameterValues.next()
        }

        val metadata = ParameterMetadata<T>(expression, part.type, value, provider)
        expressions.add(metadata)
        return metadata
    }
}

class ParameterMetadata<T: Any>(val expression: FieldExpression<T>,
                                type: Part.Type,
                                val value: Any?,
                                val provider: RequeryPersistenceProvider) {

    companion object {
        private val log = KotlinLogging.logger { }

        @JvmField val PLACEHOLDER = Any()
    }

    private val type = when {
        value == null && Part.Type.SIMPLE_PROPERTY == type -> Part.Type.IS_NULL
        else -> type
    }

    /**
     * Returns whether the parameter shall be considered an {@literal IS NULL} parameter.
     */
    val isNullParameter: Boolean = Part.Type.IS_NULL == this.type

    /**
     * Prepares the object before it's actually bound to the {@link javax.persistence.Query;}.
     */
    fun prepare(value: Any): Any? {

        val expressionType = expression.classType
        log.trace { "Prepare value... type=$type, value=$value, expressionType=$expressionType" }

        if(expressionType == String::class.java) {

            return when(type) {
                Part.Type.STARTING_WITH -> "$value%"

                Part.Type.ENDING_WITH -> "%$value"

                Part.Type.CONTAINING,
                Part.Type.NOT_CONTAINING -> "%$value%"

                else -> value
            }
        }

        if(Collection::class.java.isAssignableFrom(expressionType)) {
            return provider.potentiallyConvertEmptyCollection(value.toCollection())
        }

        return value
    }


    fun Any?.toCollection(): Collection<*>? {

        if(this == null)
            return null

        if(value is Collection<*>) {
            return value
        }

        if(ObjectUtils.isArray(value)) {
            return ObjectUtils.toObjectArray(value).toList()
        }

        return Collections.singleton(value)
    }
}