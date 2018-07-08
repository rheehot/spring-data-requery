package org.springframework.data.requery.repository.support;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.util.Assert;

/**
 * Special adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of
 * repository factories via Spring configuration.
 *
 * @author debop
 * @since 18. 6. 6
 */
@Slf4j
public class RequeryRepositoryFactoryBean<T extends Repository<S, ID>, S, ID>
    extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {

    private @Nullable RequeryOperations operations;

    public RequeryRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Autowired(required = false)
    public void setOperations(RequeryOperations operations) {
        this.operations = operations;
    }

    @Override
    protected void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {

        Assert.state(operations != null, "RequeryOperations must not be null!");

        return createRepositoryFactory(operations);
    }

    protected RepositoryFactorySupport createRepositoryFactory(RequeryOperations requeryOperations) {
        return new RequeryRepositoryFactory(requeryOperations);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(operations != null, "RequeryOperations must not be null!");

        log.debug("Before afterPropertiesSet");
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            log.debug("Fail to setup...", e);
        }
        log.debug("After afterPropertiesSet");
    }
}
