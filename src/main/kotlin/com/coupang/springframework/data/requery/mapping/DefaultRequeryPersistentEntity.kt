package com.coupang.springframework.data.requery.mapping

import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation

/**
 * DefaultRequeryPersistentEntity
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
open class DefaultRequeryPersistentEntity<T>(private val information: TypeInformation<T>)
    : BasicPersistentEntity<T, RequeryPersistentProperty>(information) {


}