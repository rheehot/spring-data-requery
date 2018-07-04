package org.springframework.data.requery.repository.query

import io.requery.query.FieldExpression
import io.requery.query.LogicalCondition
import io.requery.query.NamedExpression
import io.requery.query.element.QueryElement
import io.requery.query.function.Count
import mu.KotlinLogging
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.ReturnedType
import org.springframework.data.repository.query.parser.AbstractQueryCreator
import org.springframework.data.repository.query.parser.Part
import org.springframework.data.repository.query.parser.Part.Type.*
import org.springframework.data.repository.query.parser.PartTree
import org.springframework.data.requery.NotSupportedException
import org.springframework.data.requery.applySort
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.query.Expressions
import org.springframework.data.requery.unwrap

/**
 * Query creator to create a [QueryElement] from a [PartTree].
 *
 * @author debop@coupang.com
 */
open class RequeryQueryCreator(val operations: RequeryOperations,
                               val provider: ParameterMetadataProvider,
                               val returnedType: ReturnedType,
                               val tree: PartTree): AbstractQueryCreator<QueryElement<out Any>, LogicalCondition<out Any, *>>(tree) {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    protected val context = operations.mappingContext
    protected val domainClass = returnedType.domainType
    protected val domainClassName = domainClass.simpleName

    @Suppress("LeakingThis")
    private val root: QueryElement<out Any> = createQueryElement(returnedType)

    val parameterExpressions: List<ParameterMetadata<out Any>>
        get() = provider.getExpressions()


    protected open fun createQueryElement(type: ReturnedType): QueryElement<out Any> {

        val typeToRead = type.typeToRead
        log.debug { "Create QueryElement instance. returnedType=$type, typeToRead=$typeToRead" }

        return when {
            tree.isCountProjection -> operations.select(Count.count(type.domainType)).unwrap()
            tree.isExistsProjection -> operations.select(type.domainType).unwrap()
            tree.isDelete -> operations.delete(type.domainType).unwrap()
            else -> operations.select(type.domainType).unwrap()
        }
    }

    open override fun create(part: Part, iterator: MutableIterator<Any>): LogicalCondition<out Any, *> {

        log.trace { "Build new condition ..." }
        return buildWhereCondition(part)
    }

    override fun and(part: Part, base: LogicalCondition<out Any, *>, iterator: MutableIterator<Any>): LogicalCondition<out Any, *> {

        log.trace { "add AND operator" }

        val condition = buildWhereCondition(part)
        return base.and(condition)
    }

    override fun or(base: LogicalCondition<out Any, *>, criteria: LogicalCondition<out Any, *>): LogicalCondition<out Any, *> {

        log.trace { "add OR operator" }
        return base.or(criteria)
    }

    override fun complete(criteria: LogicalCondition<out Any, *>?, sort: Sort): QueryElement<out Any> {

        return complete(criteria, sort, root)
    }

    open fun complete(criteria: LogicalCondition<out Any, *>?, sort: Sort, base: QueryElement<out Any>): QueryElement<out Any> {

        log.trace { "Complete build query ..." }

        // TODO: returnType.needsCustomConstruction() 을 이용해서 Custom Type 에 대한 작업을 수행할 수 있다.

        val query = if(criteria != null) base.where(criteria).unwrap() else base

        return query.applySort(domainClass, sort)
    }

    private fun buildWhereCondition(part: Part): LogicalCondition<out Any, *> {
        return QueryElementBuilder(part).build()
    }


    private inner class QueryElementBuilder(val part: Part) {

        @Suppress("UNCHECKED_CAST")
        fun build(): LogicalCondition<out Any, *> {

            val property = part.property
            val type = part.type

            val expr: NamedExpression<Any> = NamedExpression.of(property.segment, property.type as Class<Any>)

            log.debug { "Build Logical condition ... property=$property, type=$type, expr=$expr" }

            return when(type) {
                BETWEEN ->
                    expr.between(provider.next(part, Comparable::class.java).value,
                                 provider.next(part, Comparable::class.java).value)

                AFTER, GREATER_THAN ->
                    expr.greaterThan(provider.next(part, Comparable::class.java).value)

                GREATER_THAN_EQUAL ->
                    expr.greaterThanOrEqual(provider.next(part, Comparable::class.java).value)

                BEFORE, LESS_THAN ->
                    expr.lessThan(provider.next(part, Comparable::class.java).value)

                LESS_THAN_EQUAL ->
                    expr.lessThanOrEqual(provider.next(part, Comparable::class.java).value)

                IS_NULL -> expr.isNull()

                IS_NOT_NULL -> expr.notNull()

                NOT_IN, IN -> {
                    val values = provider.next(part, Collection::class.java).value

                    when(values) {
                        is Iterable<*> ->
                            if(type == IN) Expressions.`in`(expr, values.toList())
                            else Expressions.notIn(expr, values.toList())

                        is Array<*> ->
                            if(type == IN) Expressions.`in`(expr, values.toList())
                            else Expressions.notIn(expr, values.toList())

                        else ->
                            if(type == IN) expr.`in`(values) else expr.notIn(values)
                    }
                }

                STARTING_WITH -> expr.like(provider.next(part, String::class.java).value.toString() + "%")

                ENDING_WITH -> expr.like("%" + provider.next(part, String::class.java).value.toString())

                CONTAINING -> expr.like("%" + provider.next(part, String::class.java).value.toString() + "%")

                NOT_CONTAINING -> expr.notLike("%" + provider.next(part, String::class.java).value.toString() + "%")

                LIKE, NOT_LIKE -> {

                    var value = provider.next(part, String::class.java).value.toString()
                    if(shouldIgnoreCase()) {
                        value = value.toUpperCase()
                    }
                    if(!value.startsWith("%") && !value.endsWith("%")) {
                        value = "%" + value + "%"
                    }

                    when(type) {
                        LIKE -> expr.upperIfIgnoreCase().like(value)
                        else -> expr.upperIfIgnoreCase().notLike(value)
                    }
                }

                TRUE -> expr.eq(true)

                FALSE -> expr.eq(false)

                SIMPLE_PROPERTY -> {
                    val simpleExpr = provider.next<Any>(part)

                    when {
                        simpleExpr.isNullParameter -> expr.isNull
                        else -> {
                            val value = if(shouldIgnoreCase()) simpleExpr.value!!.toUpperCase() else simpleExpr.value
                            expr.upperIfIgnoreCase().equal(value)
                        }
                    }
                }

                NEGATING_SIMPLE_PROPERTY -> {
                    val simpleExpr = provider.next<Any>(part)
                    val value = if(shouldIgnoreCase()) simpleExpr.value!!.toUpperCase() else simpleExpr.value
                    expr.upperIfIgnoreCase().notEqual(value)
                }

                IS_EMPTY, IS_NOT_EMPTY ->
                    throw NotSupportedException("Not supported keyword $type")
                else ->
                    throw NotSupportedException("Not supported keyword $type")
            }
        }

        private fun <T> FieldExpression<T>.upperIfIgnoreCase(): FieldExpression<T> {
            return when(part.shouldIgnoreCase()) {
                Part.IgnoreCaseType.ALWAYS -> {
                    check(this.canUppserCase()) { "Unable to ignore case of ${classType.name}" }
                    this.function("Upper")
                }
                Part.IgnoreCaseType.WHEN_POSSIBLE -> {
                    if(this.canUppserCase()) this.function("Upper") else this
                }
                Part.IgnoreCaseType.NEVER -> this
                else -> this
            }
        }

        private fun FieldExpression<*>.canUppserCase(): Boolean = this.classType == String::class.java

        fun shouldIgnoreCase(): Boolean = part.shouldIgnoreCase() != Part.IgnoreCaseType.NEVER

        fun Any.toUpperCase(): String = this.toString().toUpperCase()
    }
}