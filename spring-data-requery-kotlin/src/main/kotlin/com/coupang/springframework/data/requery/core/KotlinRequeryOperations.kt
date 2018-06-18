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

    @JvmDefault
    infix fun <T: Any> select(entityType: KClass<T>): Selection<out Result<T>> =
        dataStore.select(entityType)

    @JvmDefault
    fun <T: Any> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<out Result<T>> =
        dataStore.select(entityType, *attributes)

    @JvmDefault
    fun select(vararg expressions: Expression<*>): Selection<Result<Tuple>> =
        dataStore.select(*expressions)

    @JvmDefault
    fun <T: Any, ID> findById(entityType: KClass<T>, id: ID): T? =
        dataStore.findByKey(entityType, id)

    @JvmDefault
    infix fun <T: Any> findAll(entityType: KClass<T>): Iterable<T> =
        dataStore.select(entityType).get().toList()

    @JvmDefault
    infix fun <T: Any> refresh(entity: T): T =
        dataStore.refresh(entity)

    @JvmDefault
    fun <T: Any> refresh(entity: T, vararg attributes: Attribute<*, *>): T =
        dataStore.refresh(entity, *attributes)

    @JvmDefault
    fun <T: Any> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T> =
        dataStore.refresh<T>(entities, *attributes)

    @JvmDefault
    infix fun <T: Any> refreshAll(entity: T): T =
        dataStore.refreshAll(entity)

    @JvmDefault
    infix fun <T: Any> refreshAll(entities: Iterable<T>): Iterable<T> =
        entities.map { dataStore.refreshAll(it) }

    @JvmDefault
    infix fun <T: Any> upsert(entity: T): T =
        dataStore.upsert(entity)

    @JvmDefault
    infix fun <T: Any> upsertAll(entities: Iterable<T>): Iterable<T> =
        dataStore.upsert<T>(entities)

    @JvmDefault
    infix fun <T: Any> insert(entity: T): T =
        dataStore.insert(entity)

    @JvmDefault
    infix fun <T: Any> insert(entityType: KClass<T>): Insertion<Result<Tuple>> =
        dataStore.insert<T>(entityType)

    @JvmDefault
    fun <T: Any> insert(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): InsertInto<out Result<Tuple>> =
        dataStore.insert(entityType, *attributes)

    @JvmDefault
    infix fun <T: Any> insertAll(entities: Iterable<T>): Iterable<T> =
        dataStore.insert<T>(entities)

    @JvmDefault
    fun <T: Any, K: Any> insertAll(entities: Iterable<T>, keyClass: KClass<K>): Iterable<K> =
        dataStore.insert<K, T>(entities, keyClass)

    @JvmDefault
    fun <T: Any> update(): Update<Scalar<Int>> =
        dataStore.update()

    @JvmDefault
    infix fun <T: Any> update(entity: T): T =
        dataStore.update(entity)

    @JvmDefault
    infix fun <T: Any> update(entityType: KClass<T>): Update<Scalar<Int>> =
        dataStore.update<T>(entityType)

    @JvmDefault
    infix fun <T: Any> updateAll(entities: Iterable<T>): Iterable<T> =
        dataStore.update<T>(entities)

    @JvmDefault
    fun delete(): Deletion<Scalar<Int>> =
        dataStore.delete()

    @JvmDefault
    infix fun <T: Any> delete(entity: T) {
        dataStore.delete(entity)
    }

    @JvmDefault
    infix fun <T: Any> delete(entityType: KClass<T>): Deletion<Scalar<Int>> =
        dataStore.delete<T>(entityType)

    @JvmDefault
    infix fun <T: Any> deleteAll(entities: Iterable<T>) {
        dataStore.delete<T>(entities)
    }

    @JvmDefault
    infix fun <T: Any> deleteAll(entityType: KClass<T>): Long =
        dataStore.delete<T>(entityType).get().value().toLong()

    @JvmDefault
    infix fun <T: Any> count(entityType: KClass<T>): Selection<Scalar<Int>> =
        dataStore.count(entityType)

    @JvmDefault
    fun raw(query: String, vararg parameters: Any): Result<Tuple> =
        dataStore.raw(query, *parameters)

    @JvmDefault
    fun <T: Any> raw(entityType: KClass<T>, query: String, vararg parameters: Any): Result<T> =
        dataStore.raw(entityType, query, *parameters)

    @JvmDefault
    fun <T: Any> withTransaction(block: KotlinRequeryOperations.() -> T): T =
        withTransaction(null, block)

    fun <T: Any> withTransaction(isolation: TransactionIsolation?, block: KotlinRequeryOperations.() -> T): T

    @JvmDefault
    fun <T: Any> withDataStore(block: KotlinEntityDataStore<Any>.() -> T): T =
        block.invoke(dataStore)
}