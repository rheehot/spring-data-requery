package com.coupang.springframework.data.requery.repository.support

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.core.RequeryOperations
import com.coupang.springframework.data.requery.domain.AbstractPersistable
import com.coupang.springframework.data.requery.repository.RequeryRepository
import com.coupang.springframework.data.requery.util.paging
import com.coupang.springframework.data.requery.util.toRequeryOrder
import io.requery.kotlin.`in`
import io.requery.kotlin.eq
import org.springframework.data.domain.*
import org.springframework.stereotype.Repository
import java.util.*

/**
 * The implementation of all CRUD, paging and sorting functionality in RequeryRepository
 * from the Spring Data Commons CRUD repository and PagingAndSorting repository
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
@Repository
class SimpleRequeryRepository<T: Any, ID: Any>(private val operations: RequeryOperations,
                                               private val entityType: Class<T>): RequeryRepository<T, ID> {

    companion object: KLogging()

    // private val exampleConverter = RequeryExampleConverter(operations.converter.mappingContext)

    override fun findAll(): Iterable<T> {
        return operations.findAll(entityType)
    }

    override fun findAll(sort: Sort): Iterable<T> {
        return operations.select(entityType).orderBy(*sort.toRequeryOrder(entityType)).get().toList()
    }

    override fun <S: T> findAll(example: Example<S>): Iterable<S> {
        TODO("not implemented")
    }

    override fun <S: T> findAll(example: Example<S>, sort: Sort): Iterable<S> {
        TODO("not implemented")
    }

    override fun findAll(pageable: Pageable): Page<T> {
        val contents = operations.select(entityType).paging(entityType, pageable).get().toList()
        val totalCount = operations.count(entityType).get().value().toLong()
        return PageImpl(contents, pageable, totalCount)
    }

    override fun <S: T> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("not implemented")
    }

    override fun findAllById(ids: Iterable<ID>): Iterable<T> {
        return operations
            .select(entityType)
            .where(AbstractPersistable<ID>::id.`in`(ids.toList()))
            .get()
            .toList()
    }

    override fun <S: T> saveAll(entities: Iterable<S>): Iterable<S> {
        return operations.upsertAll(entities)
    }

    override fun <S: T> upsert(entity: S): S {
        return operations.upsert(entity)
    }

    override fun <S: T> upsertAll(entities: Iterable<S>): Iterable<S> {
        return operations.upsertAll(entities)
    }

    override fun deleteInBatch(entities: Iterable<T>) {
        operations.deleteAll(entities)
    }

    override fun deleteAllInBatch() {
        operations.deleteAll(entityType)
    }

    override fun getOne(id: ID): T? {
        return operations.findById(entityType, id)
    }

    override fun <S: T> save(entity: S): S {
        return operations.upsert(entity)
    }

    override fun deleteById(id: ID) {
        operations
            .delete(entityType)
            .where(AbstractPersistable<ID>::id.eq(id))
            .get()
    }

    override fun deleteAll(entities: MutableIterable<T>) {
        operations.deleteAll(entities)
    }

    override fun deleteAll() {
        operations.deleteAll(entityType)
    }

    override fun count(): Long {
        return operations.count(entityType).get().value().toLong()
    }

    override fun <S: T> count(example: Example<S>): Long {
        TODO("not implemented")
    }

    override fun existsById(id: ID): Boolean {
        return operations.dataStore
            .count(entityType)
            .where(AbstractPersistable<ID>::id.eq(id))
            .get()
            .value() == 1
    }

    override fun findById(id: ID): Optional<T> {
        return Optional.ofNullable(operations.findById(entityType, id))
    }

    override fun delete(entity: T) {
        operations.delete(entity)
    }

    override fun <S: T> findOne(example: Example<S>): Optional<S> {
        TODO("not implemented")
    }

    override fun <S: T> exists(example: Example<S>): Boolean {
        TODO("not implemented")
    }
}