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
 * Kotlin용 RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
interface KotlinRequeryOperations {

    val dataStore: KotlinEntityDataStore<Any>

    infix fun <T: Any> select(entityType: KClass<T>): Selection<out Result<T>> =
        dataStore.select(entityType)

    fun <T: Any> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<out Result<T>> =
        dataStore.select(entityType, *attributes)

    fun select(vararg expressions: Expression<*>): Selection<Result<Tuple>> =
        dataStore.select(*expressions)

    fun <T: Any, ID> findById(entityType: KClass<T>, id: ID): T? =
        dataStore.findByKey(entityType, id)

    infix fun <T: Any> findAll(entityType: KClass<T>): Iterable<T> =
        dataStore.select(entityType).get().toList()

    infix fun <T: Any> refresh(entity: T): T =
        dataStore.refresh(entity)

    fun <T: Any> refresh(entity: T, vararg attributes: Attribute<*, *>): T =
        dataStore.refresh(entity, *attributes)

    fun <T: Any> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T> =
        dataStore.refresh<T>(entities, *attributes)

    infix fun <T: Any> refreshAll(entity: T): T =
        dataStore.refreshAll(entity)

    infix fun <T: Any> refreshAll(entities: Iterable<T>): Iterable<T> =
        entities.map { dataStore.refreshAll(it) }

    infix fun <T: Any> upsert(entity: T): T =
        dataStore.upsert(entity)

    infix fun <T: Any> upsertAll(entities: Iterable<T>): Iterable<T> =
        dataStore.upsert<T>(entities)

    infix fun <T: Any> insert(entity: T): T =
        dataStore.insert(entity)

    infix fun <T: Any> insert(entityType: KClass<T>): Insertion<Result<Tuple>> =
        dataStore.insert<T>(entityType)

    fun <T: Any> insert(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): InsertInto<out Result<Tuple>> =
        dataStore.insert(entityType, *attributes)

    infix fun <T: Any> insertAll(entities: Iterable<T>): Iterable<T> =
        dataStore.insert<T>(entities)

    fun <T: Any, K: Any> insertAll(entities: Iterable<T>, keyClass: KClass<K>): Iterable<K> =
        dataStore.insert<K, T>(entities, keyClass)

    fun <T: Any> update(): Update<Scalar<Int>> =
        dataStore.update()

    infix fun <T: Any> update(entity: T): T =
        dataStore.update(entity)

    infix fun <T: Any> update(entityType: KClass<T>): Update<Scalar<Int>> =
        dataStore.update<T>(entityType)

    infix fun <T: Any> updateAll(entities: Iterable<T>): Iterable<T> =
        dataStore.update<T>(entities)

    fun delete(): Deletion<Scalar<Int>> =
        dataStore.delete()

    infix fun <T: Any> delete(entity: T) {
        dataStore.delete(entity)
    }

    infix fun <T: Any> delete(entityType: KClass<T>): Deletion<Scalar<Int>> =
        dataStore.delete<T>(entityType)

    infix fun <T: Any> deleteAll(entities: Iterable<T>) {
        dataStore.delete<T>(entities)
    }

    infix fun <T: Any> deleteAll(entityType: KClass<T>): Long =
        dataStore.delete<T>(entityType).get().value().toLong()

    infix fun <T: Any> count(entityType: KClass<T>): Selection<Scalar<Int>> =
        dataStore.count(entityType)

    fun raw(query: String, vararg parameters: Any): Result<Tuple> =
        dataStore.raw(query, *parameters)

    fun <T: Any> raw(entityType: KClass<T>, query: String, vararg parameters: Any): Result<T> =
        dataStore.raw(entityType, query, *parameters)

    fun <T: Any> withTransaction(block: KotlinRequeryOperations.() -> T): T =
        withTransaction(null, block)

    fun <T: Any> withTransaction(isolation: TransactionIsolation?, block: KotlinRequeryOperations.() -> T): T

    fun <T: Any> withDataStore(block: KotlinEntityDataStore<Any>.() -> T): T =
        block.invoke(dataStore)
}