package org.springframework.data.requery.repository;

import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.core.RequeryOperations;
import org.springframework.data.requery.domain.sample.User;
import org.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.data.requery.repository.config.InfrastructureConfig;
import org.springframework.data.requery.repository.sample.UserRepository;
import org.springframework.data.requery.repository.sample.UserRepositoryImpl;
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * JavaConfigUserRepositoryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 28
 */
@ContextConfiguration(inheritLocations = false, loader = AnnotationConfigContextLoader.class)
public class JavaConfigUserRepositoryTest {

    @Configuration
    static class TestConfig extends InfrastructureConfig {

        @Autowired ApplicationContext applicationContext;
        @Autowired RequeryOperations operations;

        @Bean
        public UserRepository userRepository() throws Exception {

            RequeryRepositoryFactoryBean<UserRepository, User, Integer> factory = new RequeryRepositoryFactoryBean<>(UserRepository.class);

            factory.setOperations(operations);
            factory.setBeanFactory(applicationContext);

            factory.setCustomImplementation(new UserRepositoryImpl());

            factory.afterPropertiesSet();

            return factory.getObject();
        }
    }

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { UserRepository.class })
    static class TestRequeryRepositoryConfig extends InfrastructureConfig {

    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void doesNotPickupRequeryRepository() {

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(TestRequeryRepositoryConfig.class);
        context.getBean("requeryRepository");
        context.close();

    }
}
