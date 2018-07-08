package org.springframework.data.requery.kotlin.repository.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.kotlin.mapping.RequeryMappingContext
import org.springframework.data.requery.kotlin.repository.sample.AccountRepository
import org.springframework.data.requery.kotlin.repository.sample.RoleRepository
import org.springframework.data.requery.kotlin.repository.sample.UserRepository
import org.springframework.test.context.junit4.SpringRunner

/**
 * AbstractRepositoryConfigTest
 *
 * @author debop@coupang.com
 */
@RunWith(SpringRunner::class)
abstract class AbstractRepositoryConfigTest {

    @Autowired(required = false) lateinit var userRepository: UserRepository
    @Autowired(required = false) lateinit var roleRepository: RoleRepository
    @Autowired(required = false) lateinit var accountRepository: AccountRepository

    @Autowired lateinit var mappingContext: RequeryMappingContext

    @Test
    fun `context loading`() {
        assertThat(userRepository).isNotNull
        assertThat(roleRepository).isNotNull
        assertThat(accountRepository).isNotNull
    }

    @Test
    fun `repositories have exception translation applied`() {
        TODO("Not implemented")
    }
}