package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.repository.custom.UserCustomExtendedRepository;
import com.coupang.springframework.data.requery.repository.support.TransactionalRepositoryTest;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    @EnableRequeryRepositories(basePackageClasses = { UserCustomExtendedRepository.class })
    static class TestConfiguration extends RequeryTestConfiguration {

        @Bean
        public TransactionalRepositoryTest.DelegatingTransactionManager delegatingTransactionManager() {
            return new TransactionalRepositoryTest.DelegatingTransactionManager(transactionManager());
        }
    }

    @Autowired(required = false) UserCustomExtendedRepository userRepository;

    @Autowired TransactionalRepositoryTest.DelegatingTransactionManager transactionManager;

    @Before
    public void setup() {
        transactionManager.resetCount();
    }

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
