package org.springframework.data.requery.repository.query

import io.requery.query.Tuple
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.kotlin.annotation.Query
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.data.requery.kotlin.domain.RandomData
import org.springframework.data.requery.kotlin.domain.sample.User
import org.springframework.data.requery.repository.RequeryRepository
import org.springframework.data.requery.repository.support.RequeryRepositoryFactory
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

/**
 * org.springframework.data.requery.repository.query.DeclaredRequeryQueryTest
 *
 * @author debop
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [RequeryTestConfiguration::class])
class DeclaredRequeryQueryTest {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    interface SampleQueryRepository: RequeryRepository<User, Int> {

        @Query("select * from SD_USER u where u.emailAddress=?")
        fun findByAnnotatedQuery(emailAddress: String): User?

        @Query("select * from SD_USER u where u.emailAddress like ?")
        fun findAllByEmailAddressMatches(emailAddressToMatch: String): List<User>

        @Query("select * from SD_USER limit ?")
        fun findWithLimits(limit: Int): List<User>

        @Query("select * from SD_USER u where u.firstname=? u.emailAddress=? limit ?")
        fun findAllBy(firstname: String, emailAddress: String, limit: Int): List<User>

        @Query("select u.id, u.firstname from SD_USER u where u.emailAddress=?")
        fun findAllIds(emailAddress: String): List<Tuple>

        @Query("select * from SD_USER u where u.dateOfBirth=?")
        fun findByDateOfBirth(dateOfBirth: Date): List<User>
    }

    @Autowired
    lateinit var operations: RequeryOperations

    lateinit var repository: SampleQueryRepository

    @Before
    fun setup() {
        assertThat(operations).isNotNull

        repository = RequeryRepositoryFactory(operations).getRepository(SampleQueryRepository::class.java)
        assertThat(repository).isNotNull

        repository.deleteAll()
    }

    @Test
    fun `single result from native query`() {
        val user = RandomData.randomUser()
        repository.save(user)

        val loaded = repository.findByAnnotatedQuery(user.emailAddress)

        log.debug { "loaded user=$loaded" }
        assertThat(loaded).isNotNull.isEqualTo(user)
    }

    @Test
    fun `collection result from native query`() {

        val users = RandomData.randomUsers(4)

        repository.saveAll(users)
        assertThat(repository.count()).isGreaterThan(0)

        val savedUsers = repository.findAllByEmailAddressMatches(users.first().firstname + "%")
        assertThat(savedUsers).isNotEmpty
    }

    @Test
    fun `query with limit`() {

        val users = RandomData.randomUsers(10)
        repository.saveAll(users)
        assertThat(repository.count()).isGreaterThan(0)

        val savedUsers = repository.findWithLimits(5)
        assertThat(savedUsers).hasSize(5)
    }

    // BUG: Parameter 지정에 문제가 있다.
    @Test
    fun `multiple parameter query`() {

        val users = RandomData.randomUsers(10)
        repository.saveAll(users)
        assertThat(repository.count()).isGreaterThan(0)

        val user = users.last()

        val saved = repository.findAllBy(user.firstname, user.emailAddress, 1)
        assertThat(saved).hasSize(1)
        assertThat(saved.first()).isEqualTo(user)
    }

    @Test
    fun `query tuples`() {

        val users = RandomData.randomUsers(10)
        repository.saveAll(users)
        assertThat(repository.count()).isGreaterThan(0)

        val user = users.last()

        val tuple = repository.findAllIds(user.emailAddress)
        assertThat(tuple).hasSize(1)
        assertThat(tuple[0].get<Int>("id")).isEqualTo(user.id)
        assertThat(tuple[0].get<String>("firstname")).isEqualTo(user.firstname)
    }

    @Test
    fun `query by Date`() {

        val user = RandomData.randomUser()
        repository.save(user)

        val savedUsers = repository.findByDateOfBirth(user.dateOfBirth!!)
        assertThat(savedUsers).hasSize(1)
        assertThat(savedUsers.first()).isEqualTo(user)

        val notExists = repository.findByDateOfBirth(Date())
        assertThat(notExists).isEmpty()
    }
}