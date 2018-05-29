package com.coupang.springframework.data.requery.repository

import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.QueryByExampleExecutor

/**
 * RequeryRepository
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
@NoRepositoryBean
interface RequeryRepository<T, ID>: PagingAndSortingRepository<T, ID>, QueryByExampleExecutor<T> {

    override fun findAll(): Iterable<T>

    override fun findAll(sort: Sort): Iterable<T>

    override fun findAllById(ids: Iterable<ID>): Iterable<T>

    override fun <S: T> saveAll(entities: Iterable<S>): Iterable<S>

    fun <S: T> upsert(entity: S): S

    fun <S: T> upsertAll(entities: Iterable<S>): Iterable<S>

    fun deleteInBatch(entities: Iterable<T>)

    fun deleteAllInBatch()

    fun getOne(id: ID): T

    override fun <S: T> findAll(example: Example<S>): Iterable<S>

    override fun <S: T> findAll(example: Example<S>, sort: Sort): Iterable<S>
}