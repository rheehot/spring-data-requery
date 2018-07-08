package org.springframework.data.requery.kotlin.repository

import io.requery.query.Condition
import io.requery.query.Result
import io.requery.query.Return
import io.requery.query.element.QueryElement
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import java.util.*

/**
 * org.springframework.data.requery.kotlin.repository.RequeryConditionExecutor
 *
 * @author debop
 */
@NoRepositoryBean
interface RequeryConditionExecutor<E: Any> {

    fun findOne(filter: Return<out Result<E>>): Optional<E>

    fun findAll(filter: Return<out Result<E>>): List<E>

    fun findAll(filter: QueryElement<out Result<E>>, pageable: Pageable): Page<E>

    fun findAll(conditions: Iterable<Condition<E, *>>): List<E>

    fun findAll(conditions: Iterable<Condition<E, *>>, pageable: Pageable): Page<E>

    fun findAll(conditions: Iterable<Condition<E, *>>, sort: Sort): List<E>


    fun count(queryElement: QueryElement<out Result<E>>): Long

    fun exists(queryElement: QueryElement<out Result<E>>): Boolean
}