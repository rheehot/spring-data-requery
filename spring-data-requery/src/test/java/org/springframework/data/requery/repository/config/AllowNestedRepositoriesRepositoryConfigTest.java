package org.springframework.data.requery.repository.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.repository.sample.ClassWithNestedRepository;
import org.springframework.data.requery.repository.sample.basic.BasicUserRepository;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AllowNestedRepositoriesRepositoryConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 14
 */
@ContextConfiguration
public class AllowNestedRepositoriesRepositoryConfigTest extends AbstractRepositoryConfigTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = { BasicUserRepository.class, ClassWithNestedRepository.class },
                               considerNestedRepositories = true)
    static class TestConfiguration extends InfrastructureConfig {

    }

    @Autowired ClassWithNestedRepository.NestedUserRepository nestedUserRepository;


    @Test
    public void shouldFindNestedRepository() {
        assertThat(nestedUserRepository).isNotNull();
    }

}
