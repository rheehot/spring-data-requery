package org.springframework.data.requery.repository.config;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.configs.RequeryTestConfiguration;
import org.springframework.data.requery.repository.custom.CustomGenericRequeryRepository;
import org.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactoryBean;
import org.springframework.data.requery.repository.custom.UserCustomExtendedRepository;
import org.springframework.data.requery.repository.support.TransactionalRepositoryTest.DelegatingTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CustomRepositoryFactoryConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class CustomRepositoryFactoryConfigTest {

    @Configuration
    @EnableRequeryRepositories(basePackages = { "com.coupang.springframework.data.requery.repository.custom" },
                               basePackageClasses = { CustomGenericRequeryRepository.class },
                               repositoryFactoryBeanClass = CustomGenericRequeryRepositoryFactoryBean.class)
    static class TestConfiguration extends RequeryTestConfiguration {

        @Override
        public PlatformTransactionManager transactionManager() {
            return new DelegatingTransactionManager(super.transactionManager());
        }


    }

    @Autowired(required = false) UserCustomExtendedRepository userRepository;

    @Autowired DelegatingTransactionManager transactionManager;

    @Before
    public void setup() {
        transactionManager.resetCount();
    }

    // NOTE: repositoryFactoryBeanClass 를 지정해줘야 UserCustomExtendedRepository 가 제대로 injection이 된다. 
    @Test(expected = UnsupportedOperationException.class)
    public void testCustomFactoryUsed() {
        userRepository.customMethod(1L);
    }

    @Test
    public void reconfiguresTransactionalMethodWithoutGenericParameter() {

        userRepository.findAll();

        assertThat(transactionManager.getDefinition().isReadOnly()).isFalse();
        assertThat(transactionManager.getDefinition().getTimeout()).isEqualTo(10);
    }

    @Test
    public void reconfiguresTransactionalMethodWithGenericParameter() {

        userRepository.findById(1L);

        assertThat(transactionManager.getDefinition().isReadOnly()).isFalse();
        assertThat(transactionManager.getDefinition().getTimeout()).isEqualTo(10);
    }

}
