package com.coupang.springframework.data.requery.repository.support

import com.coupang.springframework.data.requery.mapping.RequeryPersistentEntity
import org.springframework.data.repository.core.support.PersistentEntityInformation
import javax.persistence.metamodel.SingularAttribute

/**
 * RequeryPersistentEntityInformation
 *
 * @author debop@coupang.com
 * @since 18. 5. 30
 */
open class RequeryPersistentEntityInformation<T, ID>(private val entity: RequeryPersistentEntity<T>)
    : PersistentEntityInformation<T, ID>(entity), RequeryEntityInformation<T, ID> {

    private val persistentEntity: RequeryPersistentEntity<T> = entity

    override fun getIdAttribute(): SingularAttribute<in T, *>? {
        TODO("not implemented")
    }

    override fun hasCompositeId(): Boolean {
        return getIdAttributeNames().count() > 1
    }

    override fun getIdAttributeNames(): Iterable<String> {
        TODO("not implemented")
    }

    override fun getCompositeIdAttributeValue(id: Any, idAttribute: String): Any? {
        TODO("not implemented")
    }

    override val entityName: String
        get() = entity.name


}