package org.springframework.data.requery.repository

import io.requery.meta.Attribute
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.QueryByExampleExecutor
import org.springframework.data.requery.core.RequeryOperations

/**
 * [Repository] for Requery
 *
 * @author debop
 */
@NoRepositoryBean
interface RequeryRepository<E: Any, ID: Any>: PagingAndSortingRepository<E, ID>,
                                              QueryByExampleExecutor<E>,
                                              RequeryConditionExecutor<E> {

    val operations: RequeryOperations

    override fun findAll(): List<E>

    override fun findAll(sort: Sort): List<E>

    override fun findAllById(ids: Iterable<ID>): List<E>

    override fun <S: E> saveAll(entities: Iterable<S>): List<S>

    fun insert(entity: E): E

    /**
     * Entity 추가 후, 발급된 Key 값을 반환합니다.
     * @param entity
     */
    fun <K> insert(entity: E, keyClass: Class<K>): K

    fun insertAll(entities: Iterable<E>): List<E>

    fun <K> insertAll(entities: Iterable<E>, keyClass: Class<K>): List<K>

    fun upsert(entity: E): E

    fun upsertAll(entities: Iterable<E>): List<E>

    fun refresh(entity: E): E

    fun refreshAllProperties(entity: E): E

    fun refreshAll(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E>

    fun refreshAllEntities(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E>

    fun deleteInBatch(entities: Iterable<E>)

    fun deleteAllInBatch(): Int

    fun getOne(id: ID): E?

    override fun <S: E> findAll(example: Example<S>): List<S>

    override fun <S: E> findAll(example: Example<S>, sort: Sort): List<S>
}