package org.springframework.data.requery.kotlin.repository.support

import java.lang.reflect.Method

/**
 * CrudMethodMetadata
 *
 * @author debop@coupang.com
 */
interface CrudMethodMetadata {

    val method: Method
}