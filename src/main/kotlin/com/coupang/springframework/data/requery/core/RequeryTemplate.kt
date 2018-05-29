package com.coupang.springframework.data.requery.core

import com.coupang.kotlinx.data.requery.models.RequeryEntity
import com.coupang.springframework.data.requery.mapping.RequeryMappingContext
import io.requery.Persistable
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
open class RequeryTemplate(override val dataStore: EntityDataStore<Persistable>,
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

    override fun <T: Persistable> findAll(entityType: Class<T>): Iterable<T> {
        return dataStore.select(entityType).get().toList()
    }

    override fun <T: RequeryEntity<ID>, ID: Serializable> existsById(entityType: Class<T>, id: ID): Boolean {
        return dataStore.findByKey(entityType, id) != null
    }

    override fun <T: Persistable> save(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T: Persistable> upsert(entity: T): T {
        return dataStore.upsert(entity)
    }

    override fun <T: Persistable> insert(entity: T): T {
        return dataStore.insert(entity)
    }

    override fun <T: Persistable> update(entity: T): T {
        return dataStore.update(entity)
    }

    override fun <T: Persistable> delete(entity: T) {
        dataStore.delete(entity)
    }

    override fun <ID: Serializable> deleteById(id: ID): Int {
        return dataStore.delete().where(RequeryEntity<ID>::id.eq(id)).get().value()
    }

    override fun <T: Persistable> deleteAll(entityType: Class<T>): Long {
        return dataStore.delete<T>(entityType).get().value().toLong()
    }

    override fun <T: Persistable> count(entityType: Class<T>): Long {
        return dataStore.count(entityType).get().value().toLong()
    }


}