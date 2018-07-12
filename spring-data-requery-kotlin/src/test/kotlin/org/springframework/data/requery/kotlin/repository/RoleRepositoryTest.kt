package org.springframework.data.requery.kotlin.repository

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.aop.support.AopUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.kotlin.domain.sample.RoleEntity
import org.springframework.data.requery.kotlin.repository.config.EnableRequeryRepositories
import org.springframework.data.requery.kotlin.repository.sample.RoleRepository
import org.springframework.data.requery.kotlin.repository.support.SimpleRequeryRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * org.springframework.data.requery.kotlin.repository.RoleRepositoryTest
 *
 * @author debop
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [RequeryTestConfiguration::class])
@Transactional
@EnableRequeryRepositories(basePackageClasses = [RoleRepository::class])
class RoleRepositoryTest {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    @Autowired
    lateinit var repository: RoleRepository

    @Test
    fun `instancing repository`() {
        assertThat(repository).isNotNull

        assertThat(AopUtils.getTargetClass(repository)).isEqualTo(SimpleRequeryRepository::class.java)
    }

    @Test
    fun `create role`() {

        val reference = RoleEntity().apply { name = "ADMIN" }
        val result = repository.save(reference)

        assertThat(result).isEqualTo(reference)
    }

    @Test
    fun `update role`() {

        val reference = RoleEntity().apply { name = "ADMIN" }
        val result = repository.save(reference)

        assertThat(result).isEqualTo(reference)

        reference.name = "USER"
        repository.save(reference)

        assertThat(repository.findById(result.id!!)).isEqualTo(Optional.of(reference))
    }

    @Test
    fun `should use implicit count query`() {

        repository.deleteAll()

        val reference = RoleEntity().apply { name = "ADMIN" }
        repository.save(reference)

        assertThat(repository.count()).isEqualTo(1L)
    }

    @Test
    fun `should use implicit exists queries`() {

        val reference = RoleEntity().apply { name = "ADMIN" }
        repository.save(reference)

        assertThat(repository.existsById(reference.id!!)).isTrue()
    }

    @Test
    fun `should use explicitly configured entity name in derived count query`() {

        repository.deleteAll()

        val reference = RoleEntity().apply { name = "ADMIN" }
        repository.save(reference)

        assertThat(repository.countByName(reference.name)).isEqualTo(1L)
    }
}