package com.coupang.springframework.data.requery.repository.config;

import com.coupang.springframework.data.requery.repository.sample.UserRepository;
import com.coupang.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * com.coupang.springframework.data.requery.repository.config.QueryLookupStrategyTest
 *
 * @author debop
 * @since 18. 6. 24
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { QueryLookupStrategyTest.TestConfiguration.class })
public class QueryLookupStrategyTest {

    @Configuration
    @EnableRequeryRepositories(
        basePackageClasses = UserRepository.class,
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND,
        includeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { UserRepository.class }) }
    )
    static class TestConfiguration extends InfrastructureConfig {}

    @Autowired ApplicationContext context;

    @Test
    public void shouldUseExplicitlyConfiguredQueryLookupStrategy() {

        RequeryRepositoryFactoryBean<?, ?, ?> factory = context.getBean("&userRepository", RequeryRepositoryFactoryBean.class);

        assertThat(ReflectionTestUtils.getField(factory, "queryLookupStrategyKey"))
            .isEqualTo(QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND);
    }
}
