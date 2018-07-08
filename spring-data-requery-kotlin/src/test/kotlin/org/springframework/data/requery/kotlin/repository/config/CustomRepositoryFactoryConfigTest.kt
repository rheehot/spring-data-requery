package org.springframework.data.requery.repository.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.repository.custom.CustomGenericRepository
import org.springframework.data.requery.repository.custom.CustomGenericRequeryRepositoryFactoryBean
import org.springframework.data.requery.repository.custom.UserCustomExtendedRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.PlatformTransactionManager

/**
 * org.springframework.data.requery.repository.config.CustomRepositoryFactoryConfigTest
 *
 * @author debop
 */
@RunWith(SpringRunner::class)
@ContextConfiguration
class CustomRepositoryFactoryConfigTest {

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = [CustomGenericRepository::class],
                               repositoryFactoryBeanClass = CustomGenericRequeryRepositoryFactoryBean::class)
    open class TestConfiguration: RequeryTestConfiguration() {

        override fun transactionManager(): PlatformTransactionManager {
            return DelegatingTransactionManager(super.transactionManager())
        }
    }

    @Autowired(required = false)
    lateinit var userRepository: UserCustomExtendedRepository

    @Autowired
    lateinit var transactionManager: DelegatingTransactionManager

    @Before
    fun setup() {
        transactionManager.resetCount()
    }

    // NOTE: repositoryFactoryBeanClass 를 지정해줘야 UserCustomExtendedRepository 가 제대로 injection이 된다.
    @Test(expected = UnsupportedOperationException::class)
    fun testCustomFactoryUsed() {
        userRepository.customMethod(1)
    }

    @Test
    fun reconfiguresTransactionalMethodWithoutGenericParameter() {

        userRepository.findAll()

        assertThat(transactionManager.definition?.isReadOnly).isFalse()
        assertThat(transactionManager.definition?.timeout).isEqualTo(10)
    }

    @Test
    fun reconfiguresTransactionalMethodWithGenericParameter() {

        userRepository.findById(1)

        assertThat(transactionManager.definition?.isReadOnly).isFalse()
        assertThat(transactionManager.definition?.timeout).isEqualTo(10)
    }
}