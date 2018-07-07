package org.springframework.data.requery.repository.query

import io.requery.query.Tuple
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.annotation.Query
import org.springframework.data.requery.configs.RequeryTestConfiguration
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.domain.sample.User
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

        @Query("select * from SD_USER u where u.firstname= ? and u.emailAddress= ? limit ?")
        fun findAllBy(firstname: String, emailAddress: String, limit: Int): User

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
        val user = User().apply { firstname = "Debop"; lastname = "Bae"; emailAddress = "debop@coupang.com" }
        repository.save(user)

        val loaded = repository.findByAnnotatedQuery(user.emailAddress)

        log.debug { "loaded user=$loaded" }
        assertThat(loaded).isNotNull.isEqualTo(user)
    }


}