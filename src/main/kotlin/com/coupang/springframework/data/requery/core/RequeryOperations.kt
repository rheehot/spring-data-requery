package com.coupang.springframework.data.requery.core

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.Persistable
import io.requery.sql.EntityDataStore
import java.io.Serializable

/**
 * RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryOperations {

    val dataStore: EntityDataStore<io.requery.Persistable>

    fun <T: Persistable, ID> findById(entityType: Class<T>, id: ID): T?

    fun <T: AbstractPersistable<ID>, ID: Serializable> findAllById(entityType: Class<T>, ids: Iterable<ID>): Iterable<T>

    fun <T: Persistable> findAll(entityType: Class<T>): Iterable<T>

    fun <T: AbstractPersistable<ID>, ID: Serializable> existsById(entityType: Class<T>, id: ID): Boolean

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

    fun <T: Persistable> deleteAll(entityType: Class<T>): Long

    fun <T: Persistable> count(entityType: Class<T>): Long


    // TODO: 구현 필요 
    //    fun <T:Persistable> refresh(entity:T)
    //    fun <T:Persistable> refresh(entities:Iterable<T>)
    //    fun <T:Persistable> refreshAll(entity:T)
}