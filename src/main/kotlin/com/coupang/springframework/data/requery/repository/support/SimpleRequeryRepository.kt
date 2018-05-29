package com.coupang.springframework.data.requery.repository.support

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.RequeryOperations
import com.coupang.springframework.data.requery.repository.RequeryRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.util.*

/**
 * The implementation of all CRUD, paging and sorting functionality in ArangoRepository from the Spring Data Commons
 * CRUD repository and PagingAndSorting repository
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
@Repository
open class SimpleRequeryRepository<T, ID>(private val requeryOperations: RequeryOperations,
                                          private val domainClass: Class<T>): RequeryRepository<T, ID> {

    companion object: KLogging()

    //private val exampleConverter = RequeryExampleConverter(requeryOperations.converter.mappingContext)


    override fun findAll(): Iterable<T> {
        TODO("not implemented")
    }

    override fun findAll(sort: Sort): Iterable<T> {
        TODO("not implemented")
    }

    override fun <S: T> findAll(example: Example<S>): Iterable<S> {
        TODO("not implemented")
    }

    override fun <S: T> findAll(example: Example<S>, sort: Sort): Iterable<S> {
        TODO("not implemented")
    }

    override fun findAll(pageable: Pageable?): Page<T> {
        TODO("not implemented")
    }

    override fun <S: T> findAll(example: Example<S>?, pageable: Pageable?): Page<S> {
        TODO("not implemented")
    }

    override fun findAllById(ids: Iterable<ID>): Iterable<T> {
        TODO("not implemented")
    }

    override fun <S: T> saveAll(entities: Iterable<S>): Iterable<S> {
        TODO("not implemented")
    }

    override fun <S: T> upsert(entity: S): S {
        TODO("not implemented")
    }

    override fun <S: T> upsertAll(entities: Iterable<S>): Iterable<S> {
        TODO("not implemented")
    }

    override fun deleteInBatch(entities: Iterable<T>) {
        TODO("not implemented")
    }

    override fun deleteAllInBatch() {
        TODO("not implemented")
    }

    override fun getOne(id: ID): T {
        TODO("not implemented")
    }

    override fun <S: T> save(entity: S): S {
        TODO("not implemented")
    }

    override fun deleteById(id: ID) {
        TODO("not implemented")
    }

    override fun deleteAll(entities: MutableIterable<T>?) {
        TODO("not implemented")
    }

    override fun deleteAll() {
        TODO("not implemented")
    }

    override fun count(): Long {
        TODO("not implemented")
    }

    override fun <S: T> count(example: Example<S>?): Long {
        TODO("not implemented")
    }

    override fun existsById(id: ID): Boolean {
        TODO("not implemented")
    }

    override fun findById(id: ID): Optional<T> {
        TODO("not implemented")
    }

    override fun delete(entity: T) {
        TODO("not implemented")
    }

    override fun <S: T> findOne(example: Example<S>?): Optional<S> {
        TODO("not implemented")
    }

    override fun <S: T> exists(example: Example<S>?): Boolean {
        TODO("not implemented")
    }
}