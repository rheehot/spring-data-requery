package org.springframework.data.requery.repository.support

import io.requery.meta.Attribute
import io.requery.query.Condition
import io.requery.query.Result
import io.requery.query.Return
import io.requery.query.element.QueryElement
import io.requery.query.function.Count
import mu.KotlinLogging
import org.springframework.data.domain.*
import org.springframework.data.requery.*
import org.springframework.data.requery.core.RequeryOperations
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
class SimpleRequeryRepository<E: Any, ID: Any>(final val entityInformation: RequeryEntityInformation<E, ID>,
                                               override val operations: RequeryOperations): RequeryRepositoryImplementation<E, ID> {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    final val domainClass: Class<E> = entityInformation.javaType
    final val domainClassName: String = domainClass.simpleName ?: "Unknown"

    private var crudMethodMetadata: CrudMethodMetadata? = null

    override fun setRepositoryMethodMetadata(crudMethodMetadata: CrudMethodMetadata?) {
        this.crudMethodMetadata = crudMethodMetadata
    }

    override fun findAll(): List<E> = operations.findAll(domainClass)

    override fun findAll(sort: Sort): List<E> {
        return operations
            .select(domainClass)
            .unwrap()
            .applySort(domainClass, sort)
            .get()
            .toList()
    }

    override fun <S: E> findAll(example: Example<S>): List<S> {
        return example.buildQueryElement(operations, domainClass as Class<S>).get().toList()
    }

    override fun <S: E> findAll(example: Example<S>, sort: Sort): List<S> {
        return example
            .buildQueryElement(operations, domainClass as Class<S>)
            .applySort(domainClass as Class<S>, sort)
            .getAsResultEntity<S>()
            .toList()
    }

    override fun findAll(pageable: Pageable): Page<E> {
        log.trace { "Fild all $domainClassName with paging. pageable=$pageable" }

        return when {
            pageable.isPaged -> {
                val content = operations.select(domainClass).unwrap().applyPageable(domainClass, pageable).getAsResultEntity<E>().toList()
                val totals = operations.count(domainClass).get().value().toLong()

                PageImpl(content, pageable, totals)
            }
            else -> {
                val content = operations.select(domainClass).get().toList()
                PageImpl(content)
            }
        }
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

        log.trace { "Find all by id. ids=$ids" }

        val keyExpr = domainClass.getKeyExpression<ID>()

        return operations
            .select(domainClass)
            .where(keyExpr.`in`(ids.toSet()))
            .get()
            .toList()
    }

    override fun <S: E> saveAll(entities: Iterable<S>): List<S> {
        return operations.upsertAll(entities)
    }

    override fun insert(entity: E): E = operations.insert(entity)

    override fun <K> insert(entity: E, keyClass: Class<K>): K {
        return operations.insert(entity, keyClass).also {
            log.trace { "Insert entity, new key=$it" }
        }
    }

    override fun insertAll(entities: Iterable<E>): List<E> {
        return operations.insertAll(entities)
    }

    override fun <K> insertAll(entities: Iterable<E>, keyClass: Class<K>): List<K> {
        return operations.insertAll(entities, keyClass).also {
            log.trace { "Insert entities, new keys=$it" }
        }
    }

    override fun upsert(entity: E): E {
        return operations.upsert(entity)
    }

    override fun upsertAll(entities: Iterable<E>): List<E> {
        return operations.upsertAll(entities)
    }

    override fun refresh(entity: E): E {
        return operations.refresh(entity)
    }

    override fun refreshAllProperties(entity: E): E {
        return operations.refreshAllProperties(entity)
    }

    override fun refreshAll(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E> {
        return operations.refreshAll(entities, *attributes)
    }

    override fun refreshAllEntities(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E> {
        return operations.refreshAllEntities(entities, *attributes)
    }

    override fun deleteInBatch(entities: Iterable<E>) {
        operations.deleteAll(entities)
    }

    override fun deleteAllInBatch(): Int {
        return operations.deleteAll(domainClass)
    }

    override fun getOne(id: ID): E? {
        return operations.findById(domainClass, id)
    }

    override fun <S: E> save(entity: S): S {
        return operations.upsert(entity)
    }

    override fun deleteById(id: ID) {
        log.trace { "Delete $domainClassName by id [$id]" }

        val keyExpr = domainClass.getKeyExpression<ID>()

        val deletedCount = operations
            .delete(domainClass)
            .where(keyExpr.eq(id))
            .get()
            .value()

        log.trace { "Delete $domainClassName by id [$id]. deleted count=$deletedCount" }
    }

    override fun deleteAll(entities: MutableIterable<E>) {
        operations.deleteAll(entities)
    }

    override fun deleteAll() {
        log.debug { "Delete all entities ... domainClass=$domainClassName" }
        operations.deleteAll(domainClass)
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
        val keyExpr = domainClass.getKeyExpression<ID>()

        val tuple = operations
            .select(Count.count(domainClass))
            .where(keyExpr.eq(id))
            .get()
            .firstOrNull()

        return tuple.get<Int>(0) > 0
    }

    override fun findById(id: ID): Optional<E> {
        return Optional.ofNullable(operations.findById(domainClass, id))
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