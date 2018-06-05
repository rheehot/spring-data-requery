package com.coupang.springframework.data.requery.core.coroutines

import com.coupang.springframework.data.requery.core.RequeryOperations
import io.requery.Persistable
import io.requery.TransactionIsolation
import io.requery.meta.Attribute
import io.requery.meta.QueryAttribute
import io.requery.query.*
import io.requery.sql.EntityDataStore

/**
 * com.coupang.springframework.data.requery.core.coroutines.CoroutineRequeryOperations
 * @author debop
 * @since 18. 6. 2
 */
interface CoroutineRequeryOperations {

    val dataStore: EntityDataStore<Persistable>

    fun <T: Persistable> select(entityType: Class<T>): Selection<out Result<T>>

    fun <T: Persistable> select(entityType: Class<T>, vararg attributes: QueryAttribute<T, *>): Selection<out Result<T>>

    fun select(vararg expressions: Expression<*>): Selection<out Result<Tuple>>

    fun <T: Persistable, ID> findById(entityType: Class<T>, id: ID): T?

    fun <T: Persistable> findAll(entityType: Class<T>): Iterable<T>

    fun <T: Persistable> refresh(entity: T): T

    fun <T: Persistable> refresh(entity: T, vararg attributes: Attribute<*, *>): T

    fun <T: Persistable> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T>

    fun <T: Persistable> refreshAll(entity: T): T

    fun <T: Persistable> save(entity: T): T

    fun <T: Persistable> saveAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> upsert(entity: T): T

    fun <T: Persistable> upsertAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> insert(entity: T): T

    fun <T: Persistable> insertAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> insertInto(entityType: Class<T>): Insertion<out Result<Tuple>>

    fun <T: Persistable> insertInto(entityType: Class<T>, vararg attributes: QueryAttribute<T, *>): InsertInto<out Result<Tuple>>

    fun <T: Persistable> update(entityType: Class<T>): Update<out Scalar<Int>>

    fun <T: Persistable> update(entity: T): T

    fun <T: Persistable> updateAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> delete(entity: T)

    fun <T: Persistable> deleteAll(entities: Iterable<T>)

    fun <T: Persistable> deleteAll(entityType: Class<T>): Long

    fun <T: Persistable> count(entityType: Class<T>): Selection<out Scalar<Int>>

    fun raw(query: String, vararg parameters: Any?): Result<out Tuple>

    fun <T: Persistable> raw(entityType: Class<T>, query: String, vararg parameters: Any?): Result<out T>

    fun <T> runInTransaction(block: RequeryOperations.() -> T): T

    fun <T> runInTransaction(isolation: TransactionIsolation, block: RequeryOperations.() -> T): T

    fun <T> withDataStore(block: EntityDataStore<Persistable>.() -> T): T
}