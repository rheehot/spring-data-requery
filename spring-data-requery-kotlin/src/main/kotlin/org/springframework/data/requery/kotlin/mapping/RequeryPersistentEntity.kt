package org.springframework.data.requery.kotlin.mapping

import org.springframework.context.ApplicationContextAware
import org.springframework.data.mapping.PersistentEntity

/**
 * org.springframework.data.requery.mapping.RequeryPersistentEntity
 *
 * @author debop
 */
interface RequeryPersistentEntity<E>: PersistentEntity<E, RequeryPersistentProperty>, ApplicationContextAware {

    val singleIdProperty: RequeryPersistentProperty?

    val idProperties: Collection<RequeryPersistentProperty>

    val indexes: Collection<RequeryPersistentProperty>

    val embeddedProperties: Collection<RequeryPersistentProperty>

}