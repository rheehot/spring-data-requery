package com.coupang.springframework.data.requery.core

import com.coupang.kotlinx.data.requery.models.RequeryEntity
import java.io.Serializable

/**
 * RequeryOperations
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryOperations {

    fun <T: RequeryEntity<ID>, ID: Serializable> findById(entityType: Class<T>, id: ID): T?

    fun <T: RequeryEntity<ID>, ID: Serializable> findAllById(entityType: Class<T>, ids: Iterable<ID>): Iterable<T>

    fun <T> findAll(entityType: Class<T>): Iterable<T>

    fun <T, ID> existsById(entityType: Class<T>, id: ID): Boolean

    fun <T> save(entity: T): T

    fun <T> upsert(entity: T): T

    fun <T> insert(entity: T): T

    fun <T> update(entity: T): T

    fun <T> delete(entity: T)

    fun <ID: Serializable> deleteById(id: ID): Int

    fun <T> deleteAll(entityType: Class<T>): Long

    fun <T> count(entityType: Class<T>): Long
}