package org.springframework.data.requery.kotlin.core

import io.requery.TransactionIsolation
import io.requery.meta.Attribute
import io.requery.meta.EntityModel
import io.requery.meta.QueryAttribute
import io.requery.query.*
import io.requery.query.element.QueryElement
import io.requery.query.function.Count
import io.requery.sql.EntityContext
import io.requery.sql.EntityDataStore
import org.springframework.context.ApplicationContext
import org.springframework.data.requery.kotlin.applyWhereConditions
import org.springframework.data.requery.kotlin.getEntityContext
import org.springframework.data.requery.kotlin.getEntityModel
import org.springframework.data.requery.kotlin.mapping.RequeryMappingContext

/**
 *  Java용 RequeryOperations
 *
 * @author debop@coupang.com
 */
interface RequeryOperations {

    val applicationContext: ApplicationContext
    val dataStore: EntityDataStore<Any>
    val mappingContext: RequeryMappingContext

    @JvmDefault
    val entityModel: EntityModel
        get() = dataStore.getEntityModel()

    @JvmDefault
    val entityContext: EntityContext<Any>
        get() = dataStore.getEntityContext()

    @JvmDefault
    fun <E: Any> select(entityType: Class<E>): Selection<out Result<E>> = dataStore.select(entityType)

    @JvmDefault
    fun <E: Any> select(entityType: Class<E>, vararg attributes: QueryAttribute<*, *>): Selection<out Result<E>> =
        dataStore.select(entityType, *attributes)

    @JvmDefault
    fun select(vararg expressions: Expression<*>): Selection<out Result<Tuple>> = dataStore.select(*expressions)

    @JvmDefault
    fun <E, K> findById(entityType: Class<E>, id: K): E? = dataStore.findByKey(entityType, id)

    @JvmDefault
    fun <E: Any> findAll(entityType: Class<E>): List<E> = dataStore.select(entityType).get().toList()

    @JvmDefault
    fun <E: Any> refresh(entity: E): E = dataStore.refresh(entity)

    @JvmDefault
    fun <E: Any> refresh(entity: E, vararg attributes: Attribute<*, *>): E =
        dataStore.refresh(entity, *attributes)

    @JvmDefault
    fun <E: Any> refreshAll(entities: Iterable<E>, vararg attributes: Attribute<*, *>): List<E> =
        dataStore.refresh<E>(entities, *attributes).toList()

    @JvmDefault
    fun <E: Any> refreshAllProperties(entity: E): E = dataStore.refreshAll(entity)

    @JvmDefault
    fun <E: Any> refreshAllEntities(entities: Iterable<E>, vararg attributes: Attribute<E, *>): List<E> =
        entities.map { refreshAllProperties(it) }

    @JvmDefault
    fun <E: Any> upsert(entity: E): E = dataStore.upsert(entity)

    @JvmDefault
    fun <E: Any> upsertAll(entities: Iterable<E>): List<E> =
        dataStore.upsert<E>(entities).toList()

    @JvmDefault
    fun <E: Any> insert(entity: E): E = dataStore.insert(entity)

    @JvmDefault
    fun <E, K> insert(entity: E, keyClass: Class<K>): K = dataStore.insert(entity, keyClass)

    @JvmDefault
    fun <E: Any> insert(entityType: Class<E>): Insertion<out Result<Tuple>> = dataStore.insert<E>(entityType)

    @JvmDefault
    fun <E: Any> insert(entityType: Class<E>, vararg attributes: QueryAttribute<E, *>): InsertInto<out Result<Tuple>> =
        dataStore.insert(entityType, *attributes)

    @JvmDefault
    fun <E: Any> insertAll(entities: Iterable<E>): List<E> = dataStore.insert<E>(entities).toList()

    @JvmDefault
    fun <E: Any, K> insertAll(entities: Iterable<E>, keyClass: Class<K>): List<K> =
        dataStore.insert<K, E>(entities, keyClass).toList()

    @JvmDefault
    fun update(): Update<out Scalar<Int>> = dataStore.update()

    @JvmDefault
    fun <E: Any> update(entity: E): E = dataStore.update(entity)

    @JvmDefault
    fun <E: Any> update(entity: E, vararg attributes: Attribute<*, *>): E = dataStore.update(entity, *attributes)

    @JvmDefault
    fun <E: Any> update(entityType: Class<E>): Update<out Scalar<Int>> = dataStore.update<E>(entityType)

    @JvmDefault
    fun <E: Any> updateAll(entities: Iterable<E>): List<E> = dataStore.update<E>(entities).toList()

    @JvmDefault
    fun <E: Any> delete(): Deletion<out Scalar<Int>> = dataStore.delete()

    @JvmDefault
    fun <E: Any> delete(entityType: Class<E>): Deletion<out Scalar<Int>> = dataStore.delete<E>(entityType)

    @JvmDefault
    fun <E: Any> delete(entity: E) {
        dataStore.delete(entity)
    }

    @JvmDefault
    fun <E: Any> deleteAll(entities: Iterable<E>) {
        dataStore.delete<E>(entities)
    }

    @JvmDefault
    fun <E: Any> deleteAll(entityType: Class<E>): Int = dataStore.delete<E>(entityType).get().value()

    @JvmDefault
    fun <E: Any> count(entityType: Class<E>): Selection<out Scalar<Int>> =
        dataStore.count(entityType)

    @JvmDefault
    fun <E: Any> count(vararg attributes: QueryAttribute<*, *>): Selection<out Scalar<Int>> =
        dataStore.count(*attributes)


    @Suppress("UNCHECKED_CAST")
    @JvmDefault
    fun <E: Any> count(entityType: Class<E>, whereClause: QueryElement<out Result<E>>): Int {
        val query = select(Count.count(entityType))
            .applyWhereConditions(whereClause.whereElements)
            .unwrapQuery() as QueryElement<out Result<Tuple>>

        val tuple = query.get().first()
        return tuple.get(0)
    }

    @JvmDefault
    fun <E: Any> exists(entityType: Class<E>, whereClause: QueryElement<out Result<E>>): Boolean =
        whereClause.limit(1).get().firstOrNull() != null

    @JvmDefault
    fun raw(query: String, vararg parameters: Any): Result<Tuple> =
        dataStore.raw(query, *parameters)

    @JvmDefault
    fun <E: Any> raw(entityType: Class<E>, query: String, vararg parameters: Any): Result<E> =
        dataStore.raw(entityType, query, *parameters)

    @JvmDefault
    fun <V> runInTransaction(callable: () -> V): V = runInTransaction(null, callable)

    @JvmDefault
    fun <V> runInTransaction(isolation: TransactionIsolation?, callable: () -> V): V =
        dataStore.runInTransaction(callable, isolation)

    @JvmDefault
    fun <V> withTransaction(block: (EntityDataStore<Any>) -> V): V = withTransaction(null, block)

    @JvmDefault
    fun <V> withTransaction(isolation: TransactionIsolation?, block: (EntityDataStore<Any>) -> V): V =
        runInTransaction(isolation) { block(dataStore) }
}