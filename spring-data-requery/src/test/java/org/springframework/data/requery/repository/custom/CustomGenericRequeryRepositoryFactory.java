package org.springframework.data.requery.repository.custom;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.repository.support.RequeryEntityInformation;
import org.springframework.data.requery.repository.support.RequeryRepositoryFactory;
import org.springframework.data.requery.repository.support.SimpleRequeryRepository;

import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * org.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactory
 *
 * @author debop
 * @since 18. 6. 9
 */
public class CustomGenericRequeryRepositoryFactory extends RequeryRepositoryFactory {

    public CustomGenericRequeryRepositoryFactory(RequeryOperations operations) {
        super(operations);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected SimpleRequeryRepository<?, ?> getTargetRepository(RepositoryInformation information, RequeryOperations operations) {

        RequeryEntityInformation<Object, Serializable> entityMetadata = mock(RequeryEntityInformation.class);
        when(entityMetadata.getJavaType()).thenReturn((Class<Object>) information.getDomainType());
        return new CustomGenericRequeryRepository<>(entityMetadata, operations);

    }

    @Override
    protected @NotNull Class<?> getRepositoryBaseClass(@NotNull RepositoryMetadata metadata) {
        return CustomGenericRequeryRepository.class;
    }
}
