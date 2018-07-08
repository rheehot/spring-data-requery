package org.springframework.data.requery.kotlin.repository.query

import org.springframework.data.repository.core.EntityMetadata
import kotlin.reflect.KClass

/**
 * Requery specific extension of [EntityMetadata].
 *
 * @author debop
 */
interface RequeryEntityMetadata<E: Any>: EntityMetadata<E> {

    val kotlinType: KClass<E>

    /** the name of the entity */
    val entityName: String

    /** return the name of model */
    val modelName: String
}