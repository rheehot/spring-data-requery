package org.springframework.data.requery.mapping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.data.requery.configs.RequeryTestConfiguration
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.domain.sample.AbstractUser
import org.springframework.data.requery.repository.config.EnableRequeryRepositories
import org.springframework.data.requery.repository.sample.UserRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

/**
 * RequeryMappingContextIntegrationTest
 *
 * @author debop@coupang.com
 */
@RunWith(SpringRunner::class)
@ContextConfiguration
class RequeryMappingContextIntegrationTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = [UserRepository::class],
                               includeFilters = [ComponentScan.Filter(value = [UserRepository::class],
                                                                      type = FilterType.ASSIGNABLE_TYPE)]
    )
    class TestConfig: RequeryTestConfiguration()

    lateinit var context: RequeryMappingContext

    @Autowired lateinit var users: UserRepository
    @Autowired lateinit var operations: RequeryOperations

    @Before
    fun setup() {
        context = RequeryMappingContext()
    }

    @Test
    fun `setup mapping context correctly`() {

        val entity = context.getRequiredPersistentEntity(AbstractUser::class.java)
        assertThat(entity).isNotNull
    }
}