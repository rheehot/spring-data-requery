package org.springframework.data.requery.repository.support

import io.requery.meta.Attribute
import io.requery.query.Condition
import io.requery.query.Result
import io.requery.query.Return
import io.requery.query.element.QueryElement
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.requery.applySort
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.getAsResult
import org.springframework.data.requery.unwrap
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * SimpleRequeryRepository
 *
 * @author debop@coupang.com
 */
@Suppress("UNCHECKED_CAST")
@Repository
@Transactional(readOnly = true)
class SimpleRequeryRepository<E: Any, ID>(final val entityInformation: RequeryEntityInformation<E, ID>,
                                          override val operations: RequeryOperations): RequeryRepositoryImplementation<E, ID> {

    final val domainClass: Class<E> = entityInformation.javaType
    final val domainClassName: String = domainClass.simpleName ?: "Unknown"

    private var crudMethodMetadata: CrudMethodMetadata? = null

    override fun setRepositoryMethodMetadata(crudMethodMetadata: CrudMethodMetadata?) {
        this.crudMethodMetadata = crudMethodMetadata
    }

    override fun findAll(): List<E> = operations.findAll(domainClass)

    override fun findAll(sort: Sort): List<E> {
        return operations
            .select(domainClass).unwrap()
            .applySort(domainClass, sort)
            .getAsResult()
            .toList() as List<E>
    }

    override fun <S: E> findAll(example: Example<S>): List<S> {
        TODO("not implemented")
    }

    override fun <S: E> findAll(example: Example<S>, sort: Sort): List<S> {
        TODO("not implemented")
    }

    override fun findAll(pageable: Pageable): Page<E> {
        TODO("not implemented")
    }

    override fun <S: E> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("not implemented")
    }

    override fun findAll(condition: Return<out Result<E>>): List<E> {
        TODO("not implemented")
    }

    override fun findAll(condition: QueryElement<out Result<E>>, pageable: Pageable): Page<E> {
        TODO("not implemented")
    }

    override fun findAll(conditions: Iterable<Condition<E, *>>): List<E> {
        TODO("not implemented")
    }

    override fun findAll(conditions: Iterable<Condition<E, *>>, pageable: Pageable): Page<E> {
        TODO("not implemented")
    }

    override fun findAll(conditions: Iterable<Condition<E, *>>, sort: Sort): List<E> {
        TODO("not implemented")
    }

    override fun findAllById(ids: Iterable<ID>): List<E> {
        TODO("not implemented")
    }

    override fun <S: E> saveAll(entities: Iterable<S>): List<S> {
        TODO("not implemented")
    }

    override fun insert(entity: E): E {
        TODO("not implemented")
    }

    override fun insertRetunKey(entity: E): ID {
        TODO("not implemented")
    }

    override fun insertAll(entities: Iterable<E>): List<E> {
        TODO("not implemented")
    }

    override fun insertAllReturnKey(entities: Iterable<E>): List<ID> {
        TODO("not implemented")
    }

    override fun upsert(entity: E): E {
        TODO("not implemented")
    }

    override fun upsertAll(entities: Iterable<E>): List<E> {
        TODO("not implemented")
    }

    override fun refresh(entity: E): E {
        TODO("not implemented")
    }

    override fun refreshEntireProperty(entity: E): E {
        TODO("not implemented")
    }

    override fun refreshAll(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E> {
        TODO("not implemented")
    }

    override fun refreshAllEntireProperty(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E> {
        TODO("not implemented")
    }

    override fun deleteInBatch(entities: Iterable<E>) {
        TODO("not implemented")
    }

    override fun deleteAllInBatch(): Int {
        TODO("not implemented")
    }

    override fun getOne(id: ID): E? {
        TODO("not implemented")
    }

    override fun <S: E> save(entity: S): S {
        TODO("not implemented")
    }

    override fun deleteById(id: ID) {
        TODO("not implemented")
    }

    override fun deleteAll(entities: MutableIterable<E>) {
        TODO("not implemented")
    }

    override fun deleteAll() {
        TODO("not implemented")
    }

    override fun count(): Long {
        TODO("not implemented")
    }

    override fun <S: E> count(example: Example<S>): Long {
        TODO("not implemented")
    }

    override fun count(conditionElement: QueryElement<out Result<E>>): Int {
        TODO("not implemented")
    }

    override fun existsById(id: ID): Boolean {
        TODO("not implemented")
    }

    override fun findById(id: ID): Optional<E> {
        TODO("not implemented")
    }

    override fun delete(entity: E) {
        TODO("not implemented")
    }

    override fun <S: E> findOne(example: Example<S>): Optional<S> {
        TODO("not implemented")
    }

    override fun findOne(condition: Return<out Result<E>>): Optional<E> {
        TODO("not implemented")
    }

    override fun <S: E> exists(example: Example<S>): Boolean {
        TODO("not implemented")
    }

    override fun exists(conditionElement: QueryElement<out Result<E>>): Boolean {
        TODO("not implemented")
    }

}