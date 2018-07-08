@file:JvmName("ExampleExtensions")

package org.springframework.data.requery.kotlin

import io.requery.query.Result
import io.requery.query.element.QueryElement
import mu.KotlinLogging
import org.springframework.data.domain.Example
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.repository.query.applyExample

private val log = KotlinLogging.logger { }

@Suppress("UNCHECKED_CAST")
fun <S: E, E: Any> Example<S>.buildQueryElement(operations: RequeryOperations, domainClass: Class<E>): QueryElement<out Result<S>> {

    val root = operations.select(domainClass).unwrap()
    return root.applyExample(this) as QueryElement<out Result<S>>
}