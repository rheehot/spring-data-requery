package com.coupang.springframework.data.requery.core

import io.requery.TransactionIsolation
import io.requery.meta.Attribute
import io.requery.meta.QueryAttribute
import io.requery.query.*
import io.requery.sql.EntityDataStore

/**
 * RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryOperations {

    // TODO: default methods 로 하면 안되나?

    val dataStore: EntityDataStore<Any>

    fun <T> select(entityType: Class<T>): Selection<out Result<T>>

    fun <T> select(entityType: Class<T>, vararg attributes: QueryAttribute<T, *>): Selection<out Result<T>>

    fun select(vararg expressions: Expression<*>): Selection<out Result<Tuple>>

    fun <T, ID> findById(entityType: Class<T>, id: ID): T?

    fun <T> findAll(entityType: Class<T>): Iterable<T>

    fun <T> refresh(entity: T): T

    fun <T> refresh(entity: T, vararg attributes: Attribute<*, *>): T

    fun <T> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T>

    fun <T> refreshAll(entity: T): T

    fun <T> upsert(entity: T): T

    fun <T> upsertAll(entities: Iterable<T>): Iterable<T>

    fun <T> insert(entity: T): T

    fun <T> insertAll(entities: Iterable<T>): Iterable<T>

    fun <T> insert(entityType: Class<T>): Insertion<out Result<Tuple>>

    fun <T> insert(entityType: Class<T>, vararg attributes: QueryAttribute<T, *>): InsertInto<out Result<Tuple>>

    fun <T> update(entity: T): T

    fun <T> update(entityType: Class<T>): Update<out Scalar<Int>>

    fun <T> updateAll(entities: Iterable<T>): Iterable<T>

    fun <T> delete(entity: T)

    fun <T> delete(entityType: Class<T>): Deletion<out Scalar<Int>>

    fun <T> deleteAll(entities: Iterable<T>)

    fun <T> deleteAll(entityType: Class<T>): Long

    fun <T> count(entityType: Class<T>): Selection<out Scalar<Int>>

    fun raw(query: String, vararg parameters: Any?): Result<out Tuple>

    fun <T> raw(entityType: Class<T>, query: String, vararg parameters: Any?): Result<out T>

    fun <T> runInTransaction(block: RequeryOperations.() -> T): T

    fun <T> runInTransaction(isolation: TransactionIsolation, block: RequeryOperations.() -> T): T

    fun <T> withDataStore(block: EntityDataStore<Any>.() -> T): T

}