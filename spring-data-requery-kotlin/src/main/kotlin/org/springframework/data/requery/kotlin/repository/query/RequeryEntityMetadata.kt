package org.springframework.data.requery.kotlin.repository.query

import org.springframework.data.repository.core.EntityMetadata

/**
 * Requery specific extension of [EntityMetadata].
 *
 * @author debop
 */
interface RequeryEntityMetadata<E: Any>: EntityMetadata<E> {

    /** the name of the entity */
    val entityName: String

    /** return the name of model */
    val modelName: String
}