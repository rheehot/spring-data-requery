package com.coupang.springframework.data.requery.core.coroutines

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
 * Kotlin Coroutines를 활용한 [RequeryOperations] 입니다.
 * @author debop
 * @since 18. 6. 2
 */
interface CoroutineRequeryOperations {

    val dataStore: KotlinEntityDataStore<Any>


    suspend infix fun <T: Any> select(entityType: KClass<T>): Selection<out Result<T>> =
        dataStore.select(entityType)


    suspend fun <T: Any> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<out Result<T>> =
        dataStore.select(entityType, *attributes)


    suspend fun select(vararg expressions: Expression<*>): Selection<Result<Tuple>> =
        dataStore.select(*expressions)


    suspend fun <T: Any, ID> findById(entityType: KClass<T>, id: ID): T? =
        dataStore.findByKey(entityType, id)


    suspend infix fun <T: Any> findAll(entityType: KClass<T>): Iterable<T> =
        dataStore.select(entityType).get().toList()


    suspend infix fun <T: Any> refresh(entity: T): T =
        dataStore.refresh(entity)


    suspend fun <T: Any> refresh(entity: T, vararg attributes: Attribute<*, *>): T =
        dataStore.refresh(entity, *attributes)


    suspend fun <T: Any> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T> =
        dataStore.refresh<T>(entities, *attributes)


    suspend infix fun <T: Any> refreshAll(entity: T): T =
        dataStore.refreshAll(entity)


    suspend infix fun <T: Any> refreshAll(entities: Iterable<T>): Iterable<T> =
        entities.map { dataStore.refreshAll(it) }


    suspend infix fun <T: Any> upsert(entity: T): T =
        dataStore.upsert(entity)


    suspend infix fun <T: Any> upsertAll(entities: Iterable<T>): Iterable<T> =
        dataStore.upsert<T>(entities)


    suspend infix fun <T: Any> insert(entity: T): T =
        dataStore.insert(entity)


    suspend infix fun <T: Any> insert(entityType: KClass<T>): Insertion<Result<Tuple>> =
        dataStore.insert<T>(entityType)


    suspend fun <T: Any> insert(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): InsertInto<out Result<Tuple>> =
        dataStore.insert(entityType, *attributes)


    suspend infix fun <T: Any> insertAll(entities: Iterable<T>): Iterable<T> =
        dataStore.insert<T>(entities)


    suspend fun <T: Any, K: Any> insertAll(entities: Iterable<T>, keyClass: KClass<K>): Iterable<K> =
        dataStore.insert<K, T>(entities, keyClass)


    suspend fun <T: Any> update(): Update<Scalar<Int>> =
        dataStore.update()


    suspend infix fun <T: Any> update(entity: T): T =
        dataStore.update(entity)


    suspend infix fun <T: Any> update(entityType: KClass<T>): Update<Scalar<Int>> =
        dataStore.update<T>(entityType)


    suspend infix fun <T: Any> updateAll(entities: Iterable<T>): Iterable<T> =
        dataStore.update<T>(entities)


    suspend fun delete(): Deletion<Scalar<Int>> =
        dataStore.delete()


    suspend infix fun <T: Any> delete(entity: T) {
        dataStore.delete(entity)
    }


    suspend infix fun <T: Any> delete(entityType: KClass<T>): Deletion<Scalar<Int>> =
        dataStore.delete<T>(entityType)


    suspend infix fun <T: Any> deleteAll(entities: Iterable<T>) {
        dataStore.delete<T>(entities)
    }


    suspend infix fun <T: Any> deleteAll(entityType: KClass<T>): Long =
        dataStore.delete<T>(entityType).get().value().toLong()


    suspend infix fun <T: Any> count(entityType: KClass<T>): Selection<Scalar<Int>> =
        dataStore.count(entityType)


    suspend fun raw(query: String, vararg parameters: Any): Result<Tuple> =
        dataStore.raw(query, *parameters)


    suspend fun <T: Any> raw(entityType: KClass<T>, query: String, vararg parameters: Any): Result<T> =
        dataStore.raw(entityType, query, *parameters)


    suspend fun <T: Any> withTransaction(block: CoroutineRequeryOperations.() -> T): T =
        withTransaction(null, block)

    suspend fun <T: Any> withTransaction(isolation: TransactionIsolation?, block: CoroutineRequeryOperations.() -> T): T


    suspend fun <T: Any> withDataStore(block: KotlinEntityDataStore<Any>.() -> T): T =
        block.invoke(dataStore)

}