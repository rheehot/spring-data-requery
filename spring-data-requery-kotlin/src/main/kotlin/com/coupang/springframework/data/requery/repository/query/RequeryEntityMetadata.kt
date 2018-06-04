package com.coupang.springframework.data.requery.repository.query

import org.springframework.data.repository.core.EntityMetadata

/**
 * RequeryEntityMetadata
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryEntityMetadata<T>: EntityMetadata<T> {

    val entityName: String

}