package com.coupang.springframework.data.requery.core

import io.requery.TransactionIsolation
import io.requery.kotlin.*
import io.requery.meta.Attribute
import io.requery.query.Expression
import io.requery.query.Result
import io.requery.query.Scalar
import io.requery.query.Tuple
import io.requery.sql.KotlinEntityDataStore
import kotlin.reflect.KClass

/**
 * RequeryKotlinTemplate
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
class RequeryKotlinTemplate(override val dataStore: KotlinEntityDataStore<Any>): RequeryKotlinOperations {

    override fun <T: Any> select(entityType: KClass<T>): Selection<Result<T>> {
        return dataStore.select(entityType)
    }

    override fun <T: Any> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<Result<T>> {
        return dataStore.select(entityType, *attributes)
    }

    override fun select(vararg expressions: Expression<*>): Selection<Result<Tuple>> {
        return dataStore.select(*expressions)
    }

    override fun <T: Any, ID> findById(entityType: KClass<T>, id: ID): T? {
        return dataStore.findByKey(entityType, id)
    }

    override fun <T: Any> findAll(entityType: KClass<T>): Iterable<T> {
        return dataStore.select(entityType).get().toList()
    }

    override fun <T: Any> refresh(entity: T): T {
        return dataStore.refresh(entity)
    }

    override fun <T: Any> refresh(entity: T, vararg attributes: Attribute<*, *>): T {
        return dataStore.refresh(entity, *attributes)
    }

    override fun <T: Any> refresh(entities: Iterable<T>,
                                  vararg attributes: Attribute<*, *>): Iterable<T> {
        return dataStore.refresh<T>(entities, *attributes)
    }

    override fun <T: Any> refreshAll(entity: T): T {
        return dataStore.refreshAll(entity)
    }

    override fun <T: Any> refreshAll(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T> {
        return dataStore.refresh<T>(entities, *attributes)
    }

    override fun <T: Any> upsert(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T: Any> upsertAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.upsert<T>(entities)
    }

    override fun <T: Any> insert(entity: T): T {
        return dataStore.insert(entity)
    }

    override fun <T: Any> insert(entityType: KClass<T>): Insertion<Result<Tuple>> {
        return dataStore.insert<T>(entityType)
    }

    override fun <T: Any> insert(entityType: KClass<T>,
                                 vararg attributes: QueryableAttribute<T, *>): InsertInto<out Result<Tuple>> {
        return dataStore.insert(entityType, *attributes)
    }

    override fun <T: Any> insertAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.insert<T>(entities)
    }

    override fun <T: Any> update(): Update<Scalar<Int>> {
        return dataStore.update()
    }

    override fun <T: Any> update(entity: T): T {
        return dataStore.update(entity)
    }

    override fun <T: Any> update(entityType: KClass<T>): Update<Scalar<Int>> {
        return dataStore.update<T>(entityType)
    }

    override fun <T: Any> updateAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.update<T>(entities)
    }

    override fun <T: Any> delete(entity: T) {
        dataStore.delete(entity)
    }

    override fun <T: Any> delete(entityType: KClass<T>): Deletion<Scalar<Int>> {
        return dataStore.delete<T>(entityType)
    }

    override fun <T: Any> deleteAll(entities: Iterable<T>) {
        dataStore.delete<T>(entities)
    }

    override fun <T: Any> deleteAll(entityType: KClass<T>): Long {
        return dataStore.delete<T>(entityType).get().value().toLong()
    }

    override fun <T: Any> count(entityType: KClass<T>): Selection<Scalar<Int>> {
        return dataStore.count(entityType)
    }

    override fun raw(query: String, vararg parameters: Any): Result<out Tuple> {
        return dataStore.raw(query, *parameters)
    }

    override fun <T: Any> raw(entityType: KClass<T>, query: String, vararg parameters: Any): Result<out T> {
        return dataStore.raw(entityType, query, *parameters)
    }

    override fun <T: Any> withTransaction(block: RequeryKotlinOperations.() -> T): T {
        return dataStore.withTransaction { block.invoke(this@RequeryKotlinTemplate) }
    }

    override fun <T: Any> withTransaction(isolation: TransactionIsolation, block: RequeryKotlinOperations.() -> T): T {
        return dataStore.withTransaction(isolation) { block.invoke(this@RequeryKotlinTemplate) }
    }

    override fun <T: Any> withDataStore(block: KotlinEntityDataStore<Any>.() -> T): T {
        return block.invoke(dataStore)
    }
}