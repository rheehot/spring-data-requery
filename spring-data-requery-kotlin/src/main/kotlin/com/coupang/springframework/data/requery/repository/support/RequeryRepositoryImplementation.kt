package com.coupang.springframework.data.requery.repository.support

import com.coupang.springframework.data.requery.repository.RequeryRepository
import com.coupang.springframework.data.requery.repository.RequerySelectionExecutor
import org.springframework.data.repository.NoRepositoryBean

/**
 * RequeryRepositoryImplementation
 *
 * @author debop@coupang.com
 * @since 18. 5. 29
 */
@NoRepositoryBean
interface RequeryRepositoryImplementation<T, ID>: RequeryRepository<T, ID>, RequerySelectionExecutor<T> {

    // JPA에만 있다
    // fun setRepositoryMethodMetadata(crudMethodMetadata:CrudMethodMetadata)

}