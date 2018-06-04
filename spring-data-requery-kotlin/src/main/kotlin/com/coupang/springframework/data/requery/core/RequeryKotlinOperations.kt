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
 * RequeryKotlinOperations
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
interface RequeryKotlinOperations {

    // TODO: default methods 로 하면 안되나? 

    val dataStore: KotlinEntityDataStore<Any>

    fun <T: Any> select(entityType: KClass<T>): Selection<out Result<T>>

    fun <T: Any> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<out Result<T>>

    fun select(vararg expressions: Expression<*>): Selection<Result<Tuple>>

    fun <T: Any, ID> findById(entityType: KClass<T>, id: ID): T?

    fun <T: Any> findAll(entityType: KClass<T>): Iterable<T>

    fun <T: Any> refresh(entity: T): T

    fun <T: Any> refresh(entity: T, vararg attributes: Attribute<*, *>): T

    fun <T: Any> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T>

    fun <T: Any> refreshAll(entity: T): T

    fun <T: Any> refreshAll(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T>

    fun <T: Any> upsert(entity: T): T

    fun <T: Any> upsertAll(entities: Iterable<T>): Iterable<T>

    fun <T: Any> insert(entity: T): T

    fun <T: Any> insert(entityType: KClass<T>): Insertion<Result<Tuple>>

    fun <T: Any> insert(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): InsertInto<out Result<Tuple>>

    fun <T: Any> insertAll(entities: Iterable<T>): Iterable<T>

    fun <T: Any> update(): Update<Scalar<Int>>

    fun <T: Any> update(entity: T): T

    fun <T: Any> update(entityType: KClass<T>): Update<Scalar<Int>>

    fun <T: Any> updateAll(entities: Iterable<T>): Iterable<T>

    fun <T: Any> delete(entity: T)

    fun <T: Any> delete(entityType: KClass<T>): Deletion<Scalar<Int>>

    fun <T: Any> deleteAll(entities: Iterable<T>)

    fun <T: Any> deleteAll(entityType: KClass<T>): Long

    fun <T: Any> count(entityType: KClass<T>): Selection<Scalar<Int>>

    fun raw(query: String, vararg parameters: Any): Result<out Tuple>

    fun <T: Any> raw(entityType: KClass<T>, query: String, vararg parameters: Any): Result<out T>

    fun <T: Any> withTransaction(block: RequeryKotlinOperations.() -> T): T

    fun <T: Any> withTransaction(isolation: TransactionIsolation, block: RequeryKotlinOperations.() -> T): T

    fun <T: Any> withDataStore(block: KotlinEntityDataStore<Any>.() -> T): T
}