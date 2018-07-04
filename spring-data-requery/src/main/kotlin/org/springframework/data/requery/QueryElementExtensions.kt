package org.springframework.data.requery

import io.requery.query.*
import io.requery.query.element.LogicalOperator
import io.requery.query.element.QueryElement
import io.requery.query.element.QueryWrapper
import io.requery.query.element.WhereConditionElement
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

private object QEX {
    val log = KotlinLogging.logger { }
}

fun <V: Any> namedExpresesionOf(name: String, type: Class<V>): NamedExpression<V> =
    NamedExpression.of(name, type)

fun Return<out Any>.unwrap(): QueryElement<out Any> {
    return if(this is QueryWrapper<*>) {
        this.unwrapQuery()
    } else {
        this as QueryElement<*>
    }
}

fun QueryElement<out Any>.applyPageable(domainClass: Class<out Any>, pageable: Pageable): QueryElement<out Any> {

    if(pageable.isUnpaged) {
        return this
    }

    QEX.log.trace { "Apply paging .. domainClass=${domainClass.simpleName}, pageable=$pageable" }

    var query: QueryElement<out Any> = this

    if(pageable.sort.isSorted) {
        query = query.applySort(domainClass, pageable.sort)
    }

    if(pageable.pageSize > 0 && query.limit == null) {
        query = query.limit(pageable.pageSize).unwrap()
    }
    if(pageable.offset > 0 && query.offset == null) {
        query = query.offset(pageable.offset.toInt()).unwrap()
    }
    return query
}

fun QueryElement<out Any>.applySort(domainClass: Class<out Any>, sort: Sort): QueryElement<out Any> {

    QEX.log.trace { "Apply sort, domainClass=${domainClass.simpleName}, sort=$sort" }

    if(sort.isUnsorted) {
        return this
    }

    var query: QueryElement<out Any> = this

    sort.forEach { order ->

        val propertyName = order.property
        val direction = order.direction

        // 이미 있을 수 있다
        val orderExpr = query.orderByExpressions?.find { it.name == propertyName }

        orderExpr?.let {
            domainClass.findField(propertyName)?.let { field ->
                val expr = NamedExpression.of(propertyName, field.type)
                query = query.orderBy(if(direction.isAscending) expr.asc() else expr.desc()).unwrap()
            }
        }
    }

    return query
}

fun Selection<out Any>.applyWhereConditions(conditionElements: Set<WhereConditionElement<out Any>>): QueryElement<out Any> =
    this.unwrap().applyWhereConditions(conditionElements)

fun QueryElement<out Any>.applyWhereConditions(conditionElements: Set<WhereConditionElement<out Any>>): QueryElement<out Any> {
    if(conditionElements.isEmpty()) {
        return this
    }

    if(conditionElements.size == 1) {
        return this.where(conditionElements.first().condition).unwrap()
    }

    var whereElement: WhereAndOr<*> = this.where(conditionElements.first().condition)

    conditionElements
        .drop(1)
        .forEach { conditionElement ->
            val condition = conditionElement.condition
            val operator = conditionElement.operator

            QEX.log.trace { "Apply where condition=$condition, operator=$operator" }

            operator?.let {
                when(operator) {
                    LogicalOperator.AND ->
                        whereElement = whereElement.and(condition)
                    LogicalOperator.OR ->
                        whereElement = whereElement.or(condition)
                    LogicalOperator.NOT ->
                        whereElement = whereElement.and(condition).not()
                }
            }
        }

    return whereElement.unwrap()
}

@Suppress("UNCHECKED_CAST")
fun Return<out Any>.getAsResult(): Result<out Any> = this.get() as Result<out Any>

@Suppress("UNCHECKED_CAST")
fun <E> Return<out Any>.getAsResultEntity(): Result<E> = this.get() as Result<E>

@Suppress("UNCHECKED_CAST")
fun Return<out Any>.getAsScalarInt(): Scalar<Int> = this.get() as Scalar<Int>

fun Class<out Any>.getOrderingExpressions(sort: Sort): Array<OrderingExpression<out Any>> {

    if(sort.isUnsorted) {
        return emptyArray()
    }

    return sort.mapNotNull { order ->
        val propertyName = order.property

        this@getOrderingExpressions
            .findField(propertyName)
            ?.let { field ->
                val expr = namedExpresesionOf(propertyName, field.type)

                when(order.direction) {
                    Sort.Direction.ASC -> expr.asc()
                    else -> expr.desc()
                }
            }
    }.toTypedArray()
}

fun <E> Iterable<Condition<E, *>>.foldConditions(): LogicalCondition<E, *>? {

    var result: LogicalCondition<E, *>? = null

    this.forEach { cond ->
        when(result) {
            null -> result = cond as? LogicalCondition<E, *>
            else -> result!!.and(cond)
        }
    }
    return result
}

fun <E> Iterable<Condition<E, *>>.foldConditions(operator: LogicalOperator): LogicalCondition<E, *>? {

    var result: LogicalCondition<E, *>? = null

    this.forEach { cond ->
        when(result) {
            null -> result = cond as? LogicalCondition<E, *>
            else -> when(operator) {
                LogicalOperator.AND -> result!!.and(cond)
                LogicalOperator.OR -> result!!.or(cond)
                LogicalOperator.NOT -> result!!.and(cond).not()
            }
        }
    }

    return result
}


