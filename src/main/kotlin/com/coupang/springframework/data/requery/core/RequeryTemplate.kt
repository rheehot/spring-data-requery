package com.coupang.springframework.data.requery.core

import io.requery.Persistable
import io.requery.TransactionIsolation
import io.requery.meta.Attribute
import io.requery.meta.QueryAttribute
import io.requery.query.Result
import io.requery.query.Selection
import io.requery.sql.EntityDataStore

/**
 * [RequeryOperations]를 구현하여, Requery를 이용한 Database DML 작업을 수행합니다.
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryTemplate(override val dataStore: EntityDataStore<Persistable>): RequeryOperations {

    override fun <T: Persistable> select(entityType: Class<T>): Selection<out Result<T>> {
        return dataStore.select(entityType)
    }

    override fun <T: Persistable> select(entityType: Class<T>, vararg attributes: QueryAttribute<T, *>): Selection<out Result<T>> {
        return dataStore.select(entityType, *attributes)
    }


    override fun <T: Persistable, ID> findById(entityType: Class<T>, id: ID): T? {
        return dataStore.findByKey(entityType, id)
    }

    override fun <T: Persistable> findAll(entityType: Class<T>): Iterable<T> {
        return dataStore.select(entityType).get().toList()
    }

    override fun <T: Persistable> refresh(entity: T): T {
        return dataStore.refresh(entity)
    }

    override fun <T: Persistable> refresh(entity: T, vararg attributes: Attribute<*, *>): T {
        return dataStore.refresh(entity, *attributes)
    }

    override fun <T: Persistable> refresh(entities: Iterable<T>, vararg attributes: Attribute<*, *>): Iterable<T> {
        return dataStore.refresh(entities, *attributes)
    }

    override fun <T: Persistable> refreshAll(entity: T): T {
        return dataStore.refreshAll(entity)
    }

    override fun <T: Persistable> save(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T: Persistable> saveAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.upsert(entities)
    }

    override fun <T: Persistable> upsert(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T: Persistable> upsertAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.upsert(entities)
    }

    override fun <T: Persistable> insert(entity: T): T {
        return dataStore.insert(entity)
    }

    override fun <T: Persistable> insertAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.insert(entities)
    }

    override fun <T: Persistable> update(entity: T): T {
        return dataStore.update(entity)
    }

    override fun <T: Persistable> updateAll(entities: Iterable<T>): Iterable<T> {
        return dataStore.update(entities)
    }

    override fun <T: Persistable> delete(entity: T) {
        dataStore.delete(entity)
    }

    override fun <T: Persistable> deleteAll(entities: Iterable<T>) {
        dataStore.delete(entities)
    }

    override fun <T: Persistable> deleteAll(entityType: Class<T>): Long {
        return dataStore.delete<T>(entityType).get().value().toLong()
    }

    override fun <T: Persistable> count(entityType: Class<T>): Long {
        return dataStore.count(entityType).get().value().toLong()
    }

    override fun <T> runInTransaction(block: RequeryOperations.() -> T): T {
        return dataStore.runInTransaction { block.invoke(this) }
    }

    override fun <T> runInTransaction(isolation: TransactionIsolation, block: RequeryOperations.() -> T): T {
        return dataStore.runInTransaction({ block.invoke(this) }, isolation)
    }

    override fun <T> withDataStore(block: EntityDataStore<Persistable>.() -> T): T {
        return block.invoke(dataStore)
    }
}