package org.springframework.data.requery.repository.custom;

import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;

import java.io.Serializable;

/**
 * org.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactoryBean
 *
 * @author debop
 * @since 18. 6. 9
 */
public class CustomGenericRequeryRepositoryFactoryBean<T extends RequeryRepository<Object, Serializable>>
    extends RequeryRepositoryFactoryBean<T, Object, Serializable> {

    public CustomGenericRequeryRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(RequeryOperations requeryOperations) {
        return new CustomGenericRequeryRepositoryFactory(requeryOperations);
    }
}
