package com.coupang.springframework.data.requery.core

import io.requery.Persistable
import io.requery.TransactionIsolation
import io.requery.kotlin.QueryableAttribute
import io.requery.kotlin.Selection
import io.requery.meta.Attribute
import io.requery.query.Result
import io.requery.sql.KotlinEntityDataStore
import kotlin.reflect.KClass

/**
 * RequeryKotlinOperations
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
interface RequeryKotlinOperations {

    val dataStore: KotlinEntityDataStore<Persistable>

    fun <T: Persistable> select(entityType: KClass<T>): Selection<out Result<T>>

    fun <T: Persistable> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<out Result<T>>

    fun <T: Persistable, ID> findById(entityType: KClass<T>, id: ID): T?

    fun <T: Persistable> findAll(entityType: KClass<T>): Iterable<T>

    fun <T: Persistable> refresh(entity: T): T

    fun <T: Persistable> refresh(entity: T, vararg attributes: Attribute<*, *>): T

    fun <T: Persistable> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T>

    fun <T: Persistable> refreshAll(entity: T): T

    fun <T: Persistable> refreshAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> save(entity: T): T

    fun <T: Persistable> saveAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> upsert(entity: T): T

    fun <T: Persistable> upsertAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> insert(entity: T): T

    fun <T: Persistable> insertAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> update(entity: T): T

    fun <T: Persistable> updateAll(entities: Iterable<T>): Iterable<T>

    fun <T: Persistable> delete(entity: T)

    fun <T: Persistable> deleteAll(entities: Iterable<T>)

    fun <T: Persistable> deleteAll(entityType: KClass<T>): Long

    fun <T: Persistable> count(entityType: KClass<T>): Long

    fun <T> withTransaction(block: RequeryKotlinOperations.() -> T): T

    fun <T> withTransaction(isolation: TransactionIsolation, block: RequeryKotlinOperations.() -> T): T

    fun <T> withDataStore(block: KotlinEntityDataStore<Persistable>.() -> T): T
}