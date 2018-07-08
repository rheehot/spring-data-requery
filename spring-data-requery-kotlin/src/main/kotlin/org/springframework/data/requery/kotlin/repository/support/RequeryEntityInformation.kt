package org.springframework.data.requery.kotlin.repository.support

import io.requery.meta.Attribute
import org.springframework.data.repository.core.EntityInformation
import org.springframework.data.requery.kotlin.repository.query.RequeryEntityMetadata

/**
 * RequeryEntityInformation
 *
 * @author debop@coupang.com
 */
interface RequeryEntityInformation<E: Any, ID: Any>: EntityInformation<E, ID>, RequeryEntityMetadata<E> {

    /**
     * Returns the key attriute of the entity
     */
    // TODO: rename to getKeyAttribute()
    fun getIdAttribute(): Attribute<out E, out Any>?

    @JvmDefault
    fun getRequiredIdAttribute(): Attribute<out E, out Any> {
        return getIdAttribute()
               ?: throw IllegalArgumentException("Cound not obtain required identifier attribute for type [$entityName]")
    }

    fun hasCompositeId(): Boolean

    fun getIdAttributeNames(): Iterable<String>

    fun getCompositeIdAttributeValue(id: Any, idAttribute: String): Any?
}