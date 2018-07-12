package org.springframework.data.requery.kotlin.repository

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.kotlin.domain.sample.RoleEntity
import org.springframework.data.requery.kotlin.domain.sample.UserEntity
import org.springframework.data.requery.kotlin.repository.config.EnableRequeryRepositories
import org.springframework.data.requery.kotlin.repository.sample.RoleRepository
import org.springframework.data.requery.kotlin.repository.sample.UserRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp
import java.time.LocalDateTime

/**
 * org.springframework.data.requery.kotlin.repository.UserRepositoryFinderTest
 *
 * @author debop
 */
@RunWith(SpringRunner::class)
@ContextConfiguration
@Transactional
class UserRepositoryFinderTest {

    companion object {
        private val log = KotlinLogging.logger { }

        fun createUser(): UserEntity {
            return UserEntity().also {
                it.active = true
                it.createdAt = Timestamp.valueOf(LocalDateTime.now())
            }
        }

        fun createUser(firstname: String, lastname: String, email: String, vararg roles: RoleEntity): UserEntity {
            return createUser().also {
                it.firstname = firstname
                it.lastname = lastname
                it.emailAddress = email

                if(roles.isNotEmpty()) {
                    it.roles.addAll(roles)
                }
            }
        }
    }

    @Configuration
    @EnableRequeryRepositories(basePackageClasses = [UserRepository::class])
    class TestConfiguration: RequeryTestConfiguration()

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    lateinit var dave: UserEntity
    lateinit var carter: UserEntity
    lateinit var oliver: UserEntity

    lateinit var drummer: RoleEntity
    lateinit var guitarist: RoleEntity
    lateinit var singer: RoleEntity

    @Before
    fun setup() {

        drummer = roleRepository.save(RoleEntity().apply { name = "DRUMMER" })
        guitarist = roleRepository.save(RoleEntity().apply { name = "GUITARIST" })
        singer = roleRepository.save(RoleEntity().apply { name = "SINGER" })

        dave = userRepository.save(createUser("Dave", "Matthews", "dave@dmband.com", singer))
        carter = userRepository.save(createUser("Carter", "Beauford", "carter@dmband.com", singer, drummer))
        oliver = userRepository.save(createUser("Oliver Auguest", "Matthews", "oliver@dmband.com"))
    }

    @After
    fun cleanup() {
        userRepository.deleteAll()
        roleRepository.deleteAll()
    }

    @Test
    fun `simple custom created finder`() {

        val user = userRepository.findByEmailAddressAndLastname("dave@dmband.com", "Matthews")
        assertThat(user).isEqualTo(dave)
    }

    @Test
    fun `returns null if nothing found`() {

        val user = userRepository.findByEmailAddress("foobar")
        assertThat(user).isNull()
    }
}