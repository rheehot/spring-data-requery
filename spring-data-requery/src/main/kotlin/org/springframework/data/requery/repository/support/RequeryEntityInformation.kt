package org.springframework.data.requery.repository.support

import io.requery.meta.Attribute
import org.springframework.data.repository.core.EntityInformation
import org.springframework.data.requery.repository.query.RequeryEntityMetadata

/**
 * RequeryEntityInformation
 *
 * @author debop@coupang.com
 */
interface RequeryEntityInformation<E, ID>: EntityInformation<E, ID>, RequeryEntityMetadata<E> {

    /**
     * Returns the key attriute of the entity
     */
    // TODO: rename to getKeyAttribute()
    fun getIdAttribute(): Attribute<out E, *>?

    @JvmDefault
    fun getRequiredIdAttribute(): Attribute<out E, *> {
        return getIdAttribute()
               ?: throw IllegalArgumentException("Cound not obtain required identifier attribute for type [$entityName]")
    }

    fun hasCompositeId(): Boolean

    fun getIdAttributeNames(): Iterable<String>

    fun getCompositeIdAttributeValue(id: Any, idAttribute: String): Any?
}