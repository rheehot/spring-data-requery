package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.provider.QueryExtractor;
import com.coupang.springframework.data.requery.provider.RequeryPersistenceProvider;
import com.coupang.springframework.data.requery.repository.query.RequeryQueryLookupStrategy;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * RequeryRepositoryFactory
 *
 * @author debop@coupang.com
 * @since 18. 6. 4
 */
@Slf4j
public class RequeryRepositoryFactory extends RepositoryFactorySupport {

    private final RequeryOperations operations;
    private final QueryExtractor extractor;
    private final CrudMethodMetadataPostProcessor crudMethodMetadataPostProcessor;

    public RequeryRepositoryFactory(RequeryOperations operations) {
        Assert.notNull(operations, "operations must not be null!");
        log.info("Create RequeryRepositoryFactory with operations={}", operations);

        this.operations = operations;
        this.extractor = new RequeryPersistenceProvider(operations);
        this.crudMethodMetadataPostProcessor = new CrudMethodMetadataPostProcessor();
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        super.setBeanClassLoader(classLoader);
        this.crudMethodMetadataPostProcessor.setBeanClassLoader(classLoader);
    }

    @Override
    protected final RequeryRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation metadata) {

        RequeryRepositoryImplementation<?, ?> repository = getTargetRepository(metadata, operations);
        repository.setRepositoryMethodMetadata(crudMethodMetadataPostProcessor.getCrudMethodMetadata());

        Assert.isInstanceOf(RequeryRepositoryImplementation.class, repository);

        return repository;
    }

    @SuppressWarnings("unchecked")
    protected RequeryRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information,
                                                                        RequeryOperations operations) {

        log.debug("Get target repository. information={}", information);

        RequeryEntityInformation<?, ?> entityInformation = getEntityInformation(information.getDomainType());
        Object repository = getTargetRepositoryViaReflection(information, entityInformation, operations);

        Assert.isInstanceOf(RequeryRepositoryImplementation.class, repository);

        log.debug("Get target repository. repository={}", repository);

        return (RequeryRepositoryImplementation<?, ?>) repository;
    }

    @NotNull
    @Override
    protected Class<?> getRepositoryBaseClass(@NotNull RepositoryMetadata metadata) {
        return SimpleRequeryRepository.class;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
                                                                   EvaluationContextProvider evaluationContextProvider) {
        log.debug("Create QueryLookupStrategy by key={}", key);
        return Optional.of(RequeryQueryLookupStrategy.create(operations, key, extractor, evaluationContextProvider));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, ID> RequeryEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return (RequeryEntityInformation<T, ID>) RequeryEntityInformationSupport.getEntityInformation(domainClass, operations);
    }
}
