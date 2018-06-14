package com.coupang.springframework.data.requery.repository.custom;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * com.coupang.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactoryBean
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
