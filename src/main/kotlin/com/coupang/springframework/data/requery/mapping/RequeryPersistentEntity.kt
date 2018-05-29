package com.coupang.springframework.data.requery.mapping

import org.springframework.context.ApplicationContextAware
import org.springframework.data.mapping.PersistentEntity

/**
 * RequeryPersistentEntity
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
interface RequeryPersistentEntity<T>: PersistentEntity<T, RequeryPersistentProperty>, ApplicationContextAware {


}