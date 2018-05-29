package com.coupang.springframework.data.requery.repository

import io.requery.query.Selection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import java.awt.print.Pageable

/**
 * RequerySelectionExecutor
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequerySelectionExecutor<T> {

    fun findOne(selection: Selection<T>?): T?

    fun findAll(selection: Selection<T>?): List<T>

    fun findAll(selection: Selection<T>?, pageable: Pageable): Page<T>

    fun findAll(selection: Selection<T>?, sort: Sort): List<T>

    fun count(selection: Selection<T>?): Long
}