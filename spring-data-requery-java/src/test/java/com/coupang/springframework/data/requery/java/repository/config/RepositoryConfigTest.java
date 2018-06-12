package com.coupang.springframework.data.requery.java.repository.config;

import com.coupang.springframework.data.requery.java.configs.RequeryTestConfiguration;
import com.coupang.springframework.data.requery.java.repository.sample.basic.BasicUserRepositoryImpl;
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
    static class TestConfiguration extends RequeryTestConfiguration {

        @Bean
        BasicUserRepositoryImpl basicUserRepositoryImpl() {
            return new BasicUserRepositoryImpl(requeryContext());
        }
    }
}
