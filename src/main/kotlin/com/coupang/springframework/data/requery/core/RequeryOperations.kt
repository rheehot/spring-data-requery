package com.coupang.springframework.data.requery.core

import com.coupang.kotlinx.data.requery.models.RequeryEntity
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

    val dataStore: EntityDataStore<Persistable>

    fun <T: RequeryEntity<ID>, ID: Serializable> findById(entityType: Class<T>, id: ID): T?

    fun <T: RequeryEntity<ID>, ID: Serializable> findAllById(entityType: Class<T>, ids: Iterable<ID>): Iterable<T>

    fun <T: Persistable> findAll(entityType: Class<T>): Iterable<T>

    fun <T: RequeryEntity<ID>, ID: Serializable> existsById(entityType: Class<T>, id: ID): Boolean

    fun <T: Persistable> save(entity: T): T

    fun <T: Persistable> upsert(entity: T): T

    fun <T: Persistable> insert(entity: T): T

    fun <T: Persistable> update(entity: T): T

    fun <T: Persistable> delete(entity: T)

    fun <ID: Serializable> deleteById(id: ID): Int

    fun <T: Persistable> deleteAll(entityType: Class<T>): Long

    fun <T: Persistable> count(entityType: Class<T>): Long
}