package org.springframework.data.requery.mapping

import org.springframework.data.mapping.PersistentProperty

/**
 * [PersistentProperty] for Requery
 *
 * @author debop
 */
interface RequeryPersistentProperty: PersistentProperty<RequeryPersistentProperty> {

    override fun isIdProperty(): Boolean

    override fun isTransient(): Boolean

    override fun isAssociation(): Boolean

    fun isEmbedded(): Boolean

    fun hasIndex(): Boolean

    fun getFieldName(): String
}