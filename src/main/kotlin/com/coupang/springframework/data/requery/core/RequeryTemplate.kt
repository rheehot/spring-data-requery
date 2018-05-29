package com.coupang.springframework.data.requery.core

import com.coupang.kotlinx.data.requery.models.RequeryEntity
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext
import io.requery.kotlin.`in`
import io.requery.kotlin.eq
import io.requery.sql.EntityDataStore
import java.io.Serializable

/**
 * RequeryTemplate
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class RequeryTemplate(private val dataStore: EntityDataStore<Any>,
                           private val context: RequeryMappingContext): RequeryOperations {

    override fun <T: RequeryEntity<ID>, ID: Serializable> findById(entityType: Class<T>, id: ID): T? {
        return dataStore.findByKey(entityType, id)
    }

    override fun <T: RequeryEntity<ID>, ID: Serializable> findAllById(entityType: Class<T>, ids: Iterable<ID>): Iterable<T> {
        return dataStore.select(entityType)
            .where(RequeryEntity<ID>::id.`in`(ids.toList()))
            .get()
            .toList()
    }

    override fun <T> findAll(entityType: Class<T>): Iterable<T> {
        return dataStore.select(entityType).get().toList()
    }

    override fun <T, ID> existsById(entityType: Class<T>, id: ID): Boolean {
        return dataStore.findByKey(entityType, id) != null
    }

    override fun <T> save(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T> upsert(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T> insert(entity: T): T {
        return dataStore.insert(entity)
    }

    override fun <T> update(entity: T): T {
        return dataStore.update(entity)
    }

    override fun <T> delete(entity: T) {
        dataStore.delete(entity)
    }

    override fun <ID: Serializable> deleteById(id: ID): Int {
        return dataStore.delete().where(RequeryEntity<ID>::id.eq(id)).get().value()
    }

    override fun <T> deleteAll(entityType: Class<T>): Long {
        return dataStore.delete<T>(entityType).get().value().toLong()
    }

    override fun <T> count(entityType: Class<T>): Long {
        return dataStore.count(entityType).get().value().toLong()
    }


}