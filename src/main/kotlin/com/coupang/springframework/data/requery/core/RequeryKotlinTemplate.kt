package com.coupang.springframework.data.requery.core

import io.requery.Persistable
import io.requery.TransactionIsolation
import io.requery.kotlin.QueryableAttribute
import io.requery.kotlin.Selection
import io.requery.meta.Attribute
import io.requery.query.Expression
import io.requery.query.Result
import io.requery.query.Scalar
import io.requery.query.Tuple
import io.requery.sql.KotlinEntityDataStore
import kotlin.reflect.KClass

/**
 * RequeryKotlinTemplate
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
class RequeryKotlinTemplate(override val dataStore: KotlinEntityDataStore<Persistable>): RequeryKotlinOperations {

    override fun <T: Persistable> select(entityType: KClass<T>): Selection<Result<T>> {
        return dataStore.select(entityType)
    }

    override fun <T: Persistable> select(entityType: KClass<T>, vararg attributes: QueryableAttribute<T, *>): Selection<Result<T>> {
        return dataStore.select(entityType, *attributes)
    }

    override fun select(vararg expressions: Expression<*>): Selection<Result<Tuple>> {
        return dataStore.select(*expressions)
    }

    override fun <T: Persistable, ID> findById(entityType: KClass<T>, id: ID): T? {
        return dataStore.findByKey(entityType, id)
    }

    override fun <T: Persistable> findAll(entityType: KClass<T>): Iterable<T> {
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

    override fun <T: Persistable> refreshAll(entities: Iterable<T>): Iterable<T> {
        return entities.map { dataStore.refreshAll(it) }
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

    override fun <T: Persistable> deleteAll(entityType: KClass<T>): Long {
        return dataStore.delete(entityType).get().value().toLong()
    }

    override fun <T: Persistable> count(entityType: KClass<T>): Selection<Scalar<Int>> {
        return dataStore.count(entityType)
    }

    override fun <T> withTransaction(block: RequeryKotlinOperations.() -> T): T {
        return dataStore.withTransaction { block.invoke(this@RequeryKotlinTemplate) }
    }

    override fun <T> withTransaction(isolation: TransactionIsolation, block: RequeryKotlinOperations.() -> T): T {
        return dataStore.withTransaction(isolation) { block.invoke(this@RequeryKotlinTemplate) }
    }

    override fun <T> withDataStore(block: KotlinEntityDataStore<Persistable>.() -> T): T {
        return block.invoke(dataStore)
    }
}