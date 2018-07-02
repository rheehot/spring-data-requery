package org.springframework.data.requery.core

import io.requery.TransactionIsolation
import io.requery.meta.Attribute
import io.requery.meta.EntityModel
import io.requery.meta.QueryAttribute
import io.requery.query.*
import io.requery.query.element.QueryElement
import io.requery.sql.EntityContext
import io.requery.sql.EntityDataStore
import org.springframework.context.ApplicationContext
import org.springframework.data.requery.getEntityContext
import org.springframework.data.requery.getEntityModel
import org.springframework.data.requery.mapping.RequeryMappingContext
import java.util.concurrent.Callable
import java.util.function.Function

/**
 * RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 7. 2
 */
interface RequeryOperations {

    val applicationContext: ApplicationContext
    val dataStore: EntityDataStore<Any>
    val mappingContext: RequeryMappingContext

    @JvmDefault
    fun getEntityModel(): EntityModel = dataStore.getEntityModel()

    @JvmDefault
    fun getEntityContext(): EntityContext<Any> = dataStore.getEntityContext()

    @JvmDefault
    fun <E> select(entityType: Class<E>): Selection<out Result<E>> =
        dataStore.select(entityType)

    @JvmDefault
    fun <E> select(entityType: Class<E>, vararg attributes: QueryAttribute<*, *>): Selection<out Result<E>> =
        dataStore.select(entityType, *attributes)

    @JvmDefault
    fun select(vararg expressions: Expression<*>): Selection<out Result<Tuple>> =
        dataStore.select(*expressions)

    @JvmDefault
    fun <E, K> findById(entityType: Class<E>, id: K): E = dataStore.findByKey(entityType, id)

    @JvmDefault
    fun <E> findAll(entityType: Class<E>): List<E> = dataStore.select(entityType).get().toList()

    @JvmDefault
    fun <E> refresh(entity: E): E = dataStore.refresh(entity)

    @JvmDefault
    fun <E> refresh(entity: E, vararg attributes: Attribute<*, *>): E =
        dataStore.refresh(entity, *attributes)

    @JvmDefault
    fun <E> refresh(entities: Iterable<E>, vararg attributes: Attribute<*, *>): List<E> =
        dataStore.refresh<E>(entities, *attributes).toList()

    @JvmDefault
    fun <E> refreshAll(entity: E): E = dataStore.refreshAll(entity)

    @JvmDefault
    fun <E> upsert(entity: E): E = dataStore.upsert(entity)

    @JvmDefault
    fun <E> upsertAll(entities: Iterable<E>): List<E> =
        dataStore.upsert<E>(entities).toList()

    @JvmDefault
    fun <E> insert(entity: E): E = dataStore.insert(entity)

    @JvmDefault
    fun <E, K> insert(entity: E, keyClass: Class<K>): K = dataStore.insert(entity, keyClass)

    @JvmDefault
    fun <E> insert(entityType: Class<E>): Insertion<out Result<Tuple>> = dataStore.insert<E>(entityType)

    @JvmDefault
    fun <E> insert(entityType: Class<E>, vararg attributes: QueryAttribute<E, *>): InsertInto<out Result<Tuple>> =
        dataStore.insert(entityType, *attributes)

    @JvmDefault
    fun <E> insertAll(entities: Iterable<E>): List<E> = dataStore.insert<E>(entities).toList()

    @JvmDefault
    fun <E, K> insertAll(entities: Iterable<E>, keyClass: Class<K>): List<K> =
        dataStore.insert<K, E>(entities, keyClass).toList()

    @JvmDefault
    fun update(): Update<out Scalar<Int>> {
        return dataStore.update()
    }

    @JvmDefault
    fun <E> update(entity: E): E {
        return dataStore.update(entity)
    }

    @JvmDefault
    fun <E> update(entity: E, vararg attributes: Attribute<*, *>): E {
        return dataStore.update(entity, *attributes)
    }

    @JvmDefault
    fun <E> update(entityType: Class<E>): Update<out Scalar<Int>> {
        return dataStore.update<E>(entityType)
    }

    @JvmDefault
    fun <E> updateAll(entities: Iterable<E>): List<E> = dataStore.update<E>(entities).toList()

    fun <E> delete(): Deletion<out Scalar<Int>> {
        return dataStore.delete()
    }

    fun <E> delete(entityType: Class<E>): Deletion<out Scalar<Int>> {
        return dataStore.delete<E>(entityType)
    }

    fun <E> delete(entity: E) {
        dataStore.delete(entity)
    }

    fun <E> deleteAll(entities: Iterable<E>) {
        dataStore.delete<E>(entities)
    }

    fun <E> deleteAll(entityType: Class<E>): Int? {
        return dataStore.delete<E>(entityType).get().value()
    }

    fun <E> count(entityType: Class<E>): Selection<out Scalar<Int>> {
        return dataStore.count(entityType)
    }

    fun <E> count(vararg attributes: QueryAttribute<*, *>): Selection<out Scalar<Int>> {
        return dataStore.count(*attributes)
    }

    @JvmDefault
    fun <E> count(entityType: Class<E>, whereClause: QueryElement<out Result<E>>): Int {
        //        val query = RequeryUtils.applyWhereClause(unwrap(select(Count.count(entityType))), whereClause.whereElements)
        //        val tuple = (query as QueryElement<out Result<Tuple>>).get().first()
        //        return tuple.get(0)
        TODO("Not implemented")
    }

    @JvmDefault
    fun <E> exists(entityType: Class<E>, whereClause: QueryElement<out Result<E>>): Boolean {
        return whereClause.limit(1).get().firstOrNull() != null
    }

    @JvmDefault
    fun raw(query: String, vararg parameters: Any): Result<Tuple> {
        return dataStore.raw(query, *parameters)
    }

    @JvmDefault
    fun <E> raw(entityType: Class<E>, query: String, vararg parameters: Any): Result<E> {
        return dataStore.raw(entityType, query, *parameters)
    }

    @JvmDefault
    fun <V> runInTransaction(callable: Callable<V>): V {
        return runInTransaction(callable, null)
    }

    fun <V> runInTransaction(callable: Callable<V>, isolation: TransactionIsolation?): V

    @JvmDefault
    fun <V> withTransaction(block: Function<EntityDataStore<Any>, V>): V {
        return withTransaction(block, null)
    }

    fun <V> withTransaction(block: Function<EntityDataStore<Any>, V>, isolation: TransactionIsolation?): V
}