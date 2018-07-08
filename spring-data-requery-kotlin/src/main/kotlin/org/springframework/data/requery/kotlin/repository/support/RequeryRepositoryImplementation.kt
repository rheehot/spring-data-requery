package org.springframework.data.requery.kotlin.repository.support

import org.springframework.data.requery.kotlin.repository.RequeryRepository

/**
 * RequeryRepositoryImplementation
 *
 * @author debop@coupang.com
 */
interface RequeryRepositoryImplementation<E: Any, ID: Any>: RequeryRepository<E, ID> {

    fun setRepositoryMethodMetadata(crudMethodMetadata: CrudMethodMetadata?)
}
