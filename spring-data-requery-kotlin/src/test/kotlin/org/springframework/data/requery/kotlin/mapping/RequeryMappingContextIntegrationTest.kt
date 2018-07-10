package org.springframework.data.requery.kotlin.mapping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.domain.sample.User
import org.springframework.data.requery.kotlin.repository.config.EnableRequeryRepositories
import org.springframework.data.requery.kotlin.repository.sample.UserRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

@Ignore("현재 Spring Framework에서 무한루프가 발생한다.")
@RunWith(SpringRunner::class)
@ContextConfiguration
class RequeryMappingContextIntegrationTest {

    @Configuration
    @EnableRequeryRepositories(
        basePackageClasses = [UserRepository::class],
        includeFilters = [ComponentScan.Filter(value = [UserRepository::class], type = FilterType.ASSIGNABLE_TYPE)]
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

        val entity = context.getRequiredPersistentEntity(User::class.java)
        assertThat(entity).isNotNull
    }
}