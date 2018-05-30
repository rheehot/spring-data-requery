package com.coupang.springframework.data.requery.repository

import io.requery.query.Result
import io.requery.query.Selection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

/**
 * RequerySelectionExecutor
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequerySelectionExecutor<T> {

    fun findOne(selection: Selection<Result<T>>): T?

    fun findAll(selection: Selection<Result<T>>): List<T>

    fun findAll(selection: Selection<Result<T>>, pageable: Pageable): Page<T>

    fun findAll(selection: Selection<Result<T>>, sort: Sort): List<T>

    fun count(selection: Selection<Result<T>>): Long
}