package org.springframework.data.requery.repository.query

import io.requery.query.Condition
import io.requery.query.NamedExpression
import io.requery.query.Result
import io.requery.query.element.LogicalOperator
import io.requery.query.element.QueryElement
import mu.KotlinLogging
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.ExampleMatcher.StringMatcher.*
import org.springframework.data.support.ExampleMatcherAccessor
import org.springframework.data.util.DirectFieldAccessFallbackBeanWrapper
import org.springframework.util.LinkedMultiValueMap
import java.lang.reflect.Field


/**
 * Query by {@link org.springframework.data.domain.Example} 을 수행하기 위해,
 * Example 을 이용하여 [io.requery.query.WhereAndOr] 를 빌드하도록 합니다.
 *
 * @author debop
 */
@Suppress("UNCHECKED_CAST")
fun <E: Any> QueryElement<out Any>.applyExample(example: Example<E>): QueryElement<out Any> {

    val matcher = example.matcher
    val conditions = QueryByExampleBuilder.buildConditions(example, ExampleMatcherAccessor(matcher))

    val condition = when {
        matcher.isAllMatching -> conditions.foldConditions(LogicalOperator.AND)
        matcher.isAnyMatching -> conditions.foldConditions(LogicalOperator.OR)
        else -> null
    }

    return condition?.let {
        this.where(it).unwrap() // as QueryElement<out Result<E>>
    } ?: this
}

object QueryByExampleBuilder {

    private val log = KotlinLogging.logger { }

    val entityFields = LinkedMultiValueMap<Class<*>, Field>()

    // TODO : rename to applyExample
    @Suppress("UNCHECKED_CAST")
    fun <E: Any> getWhereAndOr(base: QueryElement<out Result<E>>, example: Example<E>): QueryElement<out Result<E>> {

        val matcher = example.matcher
        val conditions = buildConditions(example, ExampleMatcherAccessor(matcher))

        val condition = when {
            matcher.isAllMatching -> conditions.foldConditions(LogicalOperator.AND)
            matcher.isAnyMatching -> conditions.foldConditions(LogicalOperator.OR)
            else -> null
        }

        return condition?.let {
            base.where(condition).unwrap() as QueryElement<out Result<E>>
        } ?: base
    }

    @Suppress("UNCHECKED_CAST")
    fun <E: Any> buildConditions(example: Example<E>,
                                 accessor: ExampleMatcherAccessor): List<Condition<E, *>> {

        val conditions = arrayListOf<Condition<E, *>>()

        val beanWrapper = DirectFieldAccessFallbackBeanWrapper(example.probe as Any)
        val fields = example.probeType.findEntityFields()

        fields
            .filterNot {
                // Query By Example 에서 지원하지 못하는 Field 들은 제외합니다.
                it.isAssociationField() || it.isEmbeddedField() || it.isTransientField()
            }
            .filterNot { accessor.isIgnoredPath(it.name) }
            .forEach {
                val fieldName = it.name
                val fieldType = it.type as Class<Any>
                val fieldValue = beanWrapper.getPropertyValue(fieldName)

                log.trace { "Get condition from Example. field name=$fieldName, value=$fieldValue" }

                val expr: NamedExpression<Any> = namedExpressionOf(fieldName, fieldType)

                when {
                    fieldValue == null ->
                        if(accessor.nullHandler == ExampleMatcher.NullHandler.INCLUDE) {
                            conditions.add(expr.isNull as Condition<E, *>)
                        }

                    fieldType == String::class.java ->
                        conditions.add(buildStringCondition(accessor,
                                                            expr as NamedExpression<String>,
                                                            fieldName,
                                                            fieldValue as String))

                    else ->
                        conditions.add(expr.eq(fieldValue) as Condition<E, *>)
                }
            }

        return conditions
    }

    @Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
    fun <E: Any> buildStringCondition(accessor: ExampleMatcherAccessor,
                                      expression: NamedExpression<String>,
                                      fieldName: String,
                                      fieldValue: String): Condition<E, *> {

        val ignoreCase = accessor.isIgnoreCaseForPath(fieldName)
        log.trace { "Matching with ignoreCase? $ignoreCase" }

        val matcher = accessor.getStringMatcherForPath(fieldName)
        val fieldExpr = when {
            ignoreCase -> expression.function("Lower")
            else -> expression
        }

        return when(matcher) {
            DEFAULT, EXACT ->
                when {
                    ignoreCase -> fieldExpr.eq(fieldValue.toLowerCase())
                    else -> fieldExpr.eq(fieldValue)
                }
            CONTAINING ->
                when {
                    ignoreCase -> fieldExpr.like("%${fieldValue.toLowerCase()}%")
                    else -> fieldExpr.like("%$fieldValue%")
                }
            STARTING ->
                when {
                    ignoreCase -> fieldExpr.like(fieldValue.toLowerCase() + "%")
                    else -> fieldExpr.like("$fieldValue%")
                }
            ENDING ->
                when {
                    ignoreCase -> fieldExpr.like("%" + fieldValue.toLowerCase())
                    else -> fieldExpr.like("%$fieldValue")
                }

            else ->
                throw IllegalArgumentException("Unsupported STringMatcher $matcher")
        } as Condition<E, *>

    }
}

