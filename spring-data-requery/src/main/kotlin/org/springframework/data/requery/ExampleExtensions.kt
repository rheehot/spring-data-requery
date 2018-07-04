package org.springframework.data.requery

import io.requery.query.Result
import io.requery.query.element.QueryElement
import org.springframework.data.domain.Example
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.repository.query.applyExample


@Suppress("UNCHECKED_CAST")
fun <E: Any> Example<E>.buildQueryElement(operations: RequeryOperations, domainClass: Class<E>): QueryElement<out Result<E>> {

    val root = operations.select(domainClass).unwrap()
    return root.applyExample(this) as QueryElement<out Result<E>>
}