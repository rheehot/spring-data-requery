package com.coupang.springframework.data.requery.repository.support;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.domain.basic.AbstractBasicUser;
import com.coupang.springframework.data.requery.repository.RequeryRepository;
import io.requery.meta.EntityModel;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * RequeryRepositoryFactoryBeanTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class RequeryRepositoryFactoryBeanTest {

    RequeryRepositoryFactoryBean<SimpleSampleRepository, AbstractBasicUser, Long> factoryBean;

    @Mock RequeryOperations operations;
    StubRepositoryFactorySupport factory;
    @Mock ListableBeanFactory beanFactory;
    @Mock PersistenceExceptionTranslator translator;
    @Mock Repository<?, ?> repository;
    @Mock EntityModel entityModel;

    @Before
    public void setup() {
        Map<String, PersistenceExceptionTranslator> beans = new HashMap<>();
        beans.put("foo", translator);

        when(beanFactory.getBeansOfType(eq(PersistenceExceptionTranslator.class), anyBoolean(), anyBoolean()))
            .thenReturn(beans);

        when(operations.getEntityModel()).thenReturn(entityModel);

        factory = Mockito.spy(new StubRepositoryFactorySupport(repository));

        // Setup standard factory configuration
        factoryBean = new DummyRequeryRepositoryFactoryBean<>(SimpleSampleRepository.class);
        factoryBean.setOperations(operations);
    }

    @Test
    public void setsBasicInstanceCorrectly() {

        factoryBean.setBeanFactory(beanFactory);
        factoryBean.afterPropertiesSet();

        assertThat(factoryBean.getObject()).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void requiresListableBeanFactory() {

        factoryBean.setBeanFactory(mock(BeanFactory.class));

    }

    @Test(expected = IllegalArgumentException.class)
    public void preventsNullRepositoryInterface() {
        new RequeryRepositoryFactoryBean<Repository<Object, Long>, Object, Long>(null);
    }


    private class DummyRequeryRepositoryFactoryBean<T extends RequeryRepository<S, ID>, S, ID> extends RequeryRepositoryFactoryBean<T, S, ID> {

        public DummyRequeryRepositoryFactoryBean(@NotNull Class<? extends T> repositoryInterface) {
            super(repositoryInterface);
        }

        @Override
        protected RepositoryFactorySupport doCreateRepositoryFactory() {
            return factory;
        }
    }


    private interface SimpleSampleRepository extends RequeryRepository<AbstractBasicUser, Long> {

    }

    private static class StubRepositoryFactorySupport extends RepositoryFactorySupport {

        private final Repository<?, ?> repository;

        private StubRepositoryFactorySupport(Repository<?, ?> repository) {
            this.repository = repository;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getRepository(Class<T> repositoryInterface, RepositoryComposition.RepositoryFragments fragments) {
            return (T) repository;
        }

        @Override
        public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
            return null;
        }

        @Override
        protected Object getTargetRepository(RepositoryInformation metadata) {
            return null;
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return null;
        }
    }
}


