package com.coupang.springframework.data.requery.repository.support

import com.coupang.springframework.data.requery.repository.query.RequeryEntityMetadata
import org.springframework.data.repository.core.EntityInformation
import javax.persistence.metamodel.SingularAttribute

/**
 * RequeryEntityInformation
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryEntityInformation<T, ID>: EntityInformation<T, ID>, RequeryEntityMetadata<T> {

    fun getIdAttribute(): SingularAttribute<in T, *>?

    @JvmDefault
    fun getRequiredIdAttribute(): SingularAttribute<in T, *>? {
        return getIdAttribute()
               ?: throw IllegalArgumentException("Could not obtain required identifier attribute for type $entityName!")
    }

    fun hasCompositeId(): Boolean

    fun getIdAttributeNames(): Iterable<String>

    fun getCompositeIdAttributeValue(id: Any, idAttribute: String): Any?
}