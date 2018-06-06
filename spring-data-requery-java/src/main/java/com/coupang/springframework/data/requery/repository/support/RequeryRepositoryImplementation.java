package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.repository.RequeryPredicatedExecutor;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * com.coupang.springframework.data.requery.repository.support.RequeryRepositoryImplementation
 *
 * @author debop
 * @since 18. 6. 6
 */
public interface RequeryRepositoryImplementation<T, ID> extends RequeryRepository<T, ID>, RequeryPredicatedExecutor<T> {

    void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata);
}
