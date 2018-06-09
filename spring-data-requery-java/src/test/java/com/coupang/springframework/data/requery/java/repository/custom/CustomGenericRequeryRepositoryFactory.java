package com.coupang.springframework.data.requery.java.repository.custom;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.repository.support.RequeryEntityInformation;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactory;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryImplementation;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * com.coupang.springframework.data.requery.java.repository.custom.CustomGenericRequeryRepositoryFactory
 *
 * @author debop
 * @since 18. 6. 9
 */
public class CustomGenericRequeryRepositoryFactory extends RequeryRepositoryFactory {

    public CustomGenericRequeryRepositoryFactory(RequeryOperations operations) {
        super(operations);
    }

    @Override
    protected RequeryRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, RequeryOperations operations) {

        RequeryEntityInformation<Object, Serializable> entityMetadata = mock(RequeryEntityInformation.class);
        when(entityMetadata.getJavaType()).thenReturn((Class<Object>) information.getDomainType());
        return new CustomGenericRequeryRepository<Object, Serializable>(entityMetadata, operations);

    }

    @Override
    protected @NotNull Class<?> getRepositoryBaseClass(@NotNull RepositoryMetadata metadata) {
        return CustomGenericRequeryRepository.class;
    }
}
