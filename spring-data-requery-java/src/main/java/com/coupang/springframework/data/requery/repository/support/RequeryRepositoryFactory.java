package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

/**
 * RequeryRepositoryFactory
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
public class RequeryRepositoryFactory extends RepositoryFactorySupport {

    private final RequeryOperations requeryOperations;
    private final CrudMethodMetadataPostProcessor crudMethodMetadataPostProcessor;

    public RequeryRepositoryFactory(RequeryOperations requeryOperations) {
        Assert.notNull(requeryOperations, "requeryOperations must not be null!");
        log.info("Create RequeryRepositoryFactory");

        this.crudMethodMetadataPostProcessor = new CrudMethodMetadataPostProcessor();
        this.requeryOperations = requeryOperations;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        super.setBeanClassLoader(classLoader);
        this.crudMethodMetadataPostProcessor.setBeanClassLoader(classLoader);
    }

    @Override
    protected final RequeryRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation metadata) {

        RequeryRepositoryImplementation<?, ?> repository = getTargetRepository(metadata, requeryOperations);
        repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());

        Assert.isInstanceOf(RequeryRepositoryImplementation.class, repository);

        return repository;
    }

    protected SimpleRequeryRepository<?, ?> getTargetRepository(@NotNull RepositoryInformation information,
                                                                RequeryOperations operations) {
        return new SimpleRequeryRepository(requeryOperations, information.getDomainType());
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return SimpleRequeryRepository.class;
    }

    public <T, ID> RequeryEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return (RequeryEntityInformation<T, ID>) RequeryEntityInformationSupport.getEntityInformation(domainClass, requeryOperations);
    }
}
