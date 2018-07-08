package org.springframework.data.requery.repository.support;

import org.springframework.data.requery.repository.RequeryRepository;

/**
 * com.coupang.springframework.data.requery.repository.support.RequeryRepositoryImplementation
 *
 * @author debop
 * @since 18. 6. 6
 */
public interface RequeryRepositoryImplementation<T, ID> extends RequeryRepository<T, ID> {

    void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata);

}
