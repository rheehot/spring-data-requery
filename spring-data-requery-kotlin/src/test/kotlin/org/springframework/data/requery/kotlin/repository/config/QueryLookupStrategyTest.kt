package org.springframework.data.requery.repository.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.data.repository.query.QueryLookupStrategy
import org.springframework.data.requery.repository.sample.UserRepository
import org.springframework.data.requery.repository.support.RequeryRepositoryFactoryBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils

/**
 * org.springframework.data.requery.repository.config.QueryLookupStrategyTest
 *
 * @author debop
 */
@RunWith(SpringRunner::class)
@ContextConfiguration
class QueryLookupStrategyTest {

    @Configuration
    @EnableRequeryRepositories(
        basePackageClasses = [UserRepository::class],
        queryLookupStrategy = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND,
        includeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [UserRepository::class])]
    )
    class TestConfiguration: InfrastructureConfig()

    @Autowired
    lateinit var context: ApplicationContext

    @Test
    fun `should use explicitly configured QueryLookupStrategy`() {

        val factory = context.getBean("&userRepository", RequeryRepositoryFactoryBean::class.java)

        assertThat(ReflectionTestUtils.getField(factory, "queryLookupStrategyKey"))
            .isEqualTo(QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
    }
}