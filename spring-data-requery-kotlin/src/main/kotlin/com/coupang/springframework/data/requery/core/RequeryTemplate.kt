package com.coupang.springframework.data.requery.core

import io.requery.TransactionIsolation
import io.requery.meta.Attribute
import io.requery.meta.QueryAttribute
import io.requery.query.*
import io.requery.sql.EntityDataStore

/**
 * [RequeryOperations]를 구현하여, Requery를 이용한 Database DML 작업을 수행합니다.
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryTemplate(override val dataStore: EntityDataStore<Any>): RequeryOperations {

    override fun <T> select(entityType: Class<T>): Selection<out Result<T>> {
        return dataStore.select(entityType)
    }

    override fun <T> select(entityType: Class<T>,
                            vararg attributes: QueryAttribute<T, *>): Selection<out Result<T>> {
        return dataStore.select(entityType, *attributes)
    }

    override fun select(vararg expressions: Expression<*>): Selection<out Result<Tuple>> {
        return dataStore.select(*expressions)
    }

    override fun <T, ID> findById(entityType: Class<T>, id: ID): T? {
        return dataStore.findByKey(entityType, id)
    }

    override fun <T> findAll(entityType: Class<T>): Iterable<T> {
        return dataStore.select(entityType).get().toList()
    }

    override fun <T> refresh(entity: T): T {
        return dataStore.refresh(entity)
    }

    override fun <T> refresh(entity: T, vararg attributes: Attribute<*, *>): T {
        return dataStore.refresh(entity, *attributes)
    }

    override fun <T> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T> {
        return dataStore.refresh<T>(entities, *attributes)
    }

    override fun <T> refreshAll(entity: T): T {
        return dataStore.refreshAll(entity)
    }

    override fun <T> upsert(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T> upsertAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.upsert<T>(entities)
    }

    override fun <T> insert(entity: T): T {
        return dataStore.insert(entity)
    }

    override fun <T> insertAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.insert<T>(entities)
    }

    override fun <T> insert(entityType: Class<T>): Insertion<out Result<Tuple>> {
        return dataStore.insert<T>(entityType)
    }

    override fun <T> insert(entityType: Class<T>,
                            vararg attributes: QueryAttribute<T, *>): InsertInto<out Result<Tuple>> {
        return dataStore.insert(entityType, *attributes)
    }

    override fun <T> update(entityType: Class<T>): Update<out Scalar<Int>> {
        return dataStore.update<T>(entityType)
    }

    override fun <T> update(entity: T): T {
        return dataStore.update(entity)
    }

    override fun <T> updateAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.update<T>(entities)
    }

    override fun <T> delete(entity: T) {
        dataStore.delete(entity)
    }

    override fun <T> delete(entityType: Class<T>): Deletion<out Scalar<Int>> {
        return dataStore.delete<T>(entityType)
    }

    override fun <T> deleteAll(entities: Iterable<T>) {
        dataStore.delete<T>(entities)
    }

    override fun <T> deleteAll(entityType: Class<T>): Long {
        return dataStore.delete<T>(entityType).get().value().toLong()
    }

    override fun <T> count(entityType: Class<T>): Selection<out Scalar<Int>> {
        return dataStore.count(entityType)
    }

    override fun raw(query: String, vararg parameters: Any?): Result<out Tuple> {
        return dataStore.raw(query, *parameters)
    }

    override fun <T> raw(entityType: Class<T>, query: String, vararg parameters: Any?): Result<out T> {
        return dataStore.raw(entityType, query, *parameters)
    }

    override fun <T> runInTransaction(block: RequeryOperations.() -> T): T {
        return dataStore.runInTransaction { block.invoke(this) }
    }

    override fun <T> runInTransaction(isolation: TransactionIsolation, block: RequeryOperations.() -> T): T {
        return dataStore.runInTransaction({ block.invoke(this) }, isolation)
    }

    override fun <T> withDataStore(block: EntityDataStore<Any>.() -> T): T {
        return block(dataStore)
    }
}