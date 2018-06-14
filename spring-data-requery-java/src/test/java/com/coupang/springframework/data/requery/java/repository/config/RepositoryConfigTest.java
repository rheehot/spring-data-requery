package com.coupang.springframework.data.requery.java.repository.config;

import com.coupang.springframework.data.requery.java.repository.sample.basic.BasicUserRepositoryImpl;
import com.coupang.springframework.data.requery.repository.config.EnableRequeryRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    @EnableRequeryRepositories(basePackages = { "com.coupang.**.repository.sample.basic" })
    static class TestConfiguration extends InfrastructureConfig {

        @Bean
        BasicUserRepositoryImpl basicUserRepositoryImpl() {
            return new BasicUserRepositoryImpl(requeryContext());
        }
    }
}
