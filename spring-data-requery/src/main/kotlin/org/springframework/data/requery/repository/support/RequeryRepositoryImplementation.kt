package org.springframework.data.requery.repository.support

import org.springframework.data.requery.repository.RequeryRepository

/**
 * RequeryRepositoryImplementation
 *
 * @author debop@coupang.com
 */
interface RequeryRepositoryImplementation<E: Any, ID>: RequeryRepository<E, ID> {

    fun setRepositoryMethodMetadata(crudMethodMetadata: CrudMethodMetadata?)
}
