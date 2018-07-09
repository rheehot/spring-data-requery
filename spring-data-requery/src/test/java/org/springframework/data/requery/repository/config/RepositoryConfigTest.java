package org.springframework.data.requery.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.requery.repository.sample.basic.BasicUserRepositoryImpl;
import org.springframework.test.context.ContextConfiguration;

/**
 * RepositoryConfigTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
@ContextConfiguration
public class RepositoryConfigTest extends AbstractRepositoryConfigTest {


    @Configuration
    @EnableRequeryRepositories(basePackages = { "org.springframework.data.requery.repository.sample.basic" })
    static class TestConfiguration extends InfrastructureConfig {

        @Bean
        BasicUserRepositoryImpl basicUserRepositoryImpl() {
            return new BasicUserRepositoryImpl();
        }
    }
}
