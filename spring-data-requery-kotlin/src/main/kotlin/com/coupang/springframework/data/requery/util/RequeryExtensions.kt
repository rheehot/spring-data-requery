package com.coupang.springframework.data.requery.util

import io.requery.query.*
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * RequeryExtensions
 *
 * @author debop@coupang.com
 * @since 18. 5. 30
 */

/**
 * Spring Data 의 Sort 정보를 Requery용 OrderingExpression으로 표현합니다.
 */
fun <T> Sort.toRequeryOrder(entityType: Class<T>): Array<OrderingExpression<T>> {
    return this.map { order ->

        val expr = NamedExpression.of(order.property, entityType)
        val orderExpr = when {
            order.isAscending  -> expr.asc()
            order.isDescending -> expr.desc()
            else               -> expr.asc()
        }
        orderExpr
    }
        .toList()
        .toTypedArray()
}

fun <E> Selection<out Result<E>>.paging(entityType: Class<E>, pageable: Pageable): Return<out Result<E>> {

    val ordering = pageable.sort.toRequeryOrder(entityType)
    val offset = pageable.offset.toInt()
    val limit = pageable.pageSize

    return orderBy(*ordering)
        .limit(limit)
        .offset(offset)
}
