package org.springframework.data.requery.repository.support

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.requery.sql.EntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.repository.core.EntityInformation
import org.springframework.data.repository.query.QueryLookupStrategy
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.data.requery.domain.sample.User
import org.springframework.data.requery.repository.RequeryRepository
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.util.*

/**
 * RequeryRepositoryFactoryTest
 *
 * @author debop@coupang.com
 */
@RunWith(MockitoJUnitRunner.Silent::class)
class RequeryRepositoryFactoryTest {

    lateinit var factory: RequeryRepositoryFactory

    val operations = mock<RequeryOperations>()
    val dataStore = mock<EntityDataStore<Any>>()
    val entityInformation = mock<RequeryEntityInformation<out Any, out Any>>()

    @Before
    fun setup() {
        whenever(operations.dataStore).doReturn(dataStore)

        whenever(entityInformation.javaType).doReturn(User::class.java)

        factory = object: RequeryRepositoryFactory(operations) {
            @Suppress("UNCHECKED_CAST")
            override fun <E: Any, ID: Any> getEntityInformation(domainClass: Class<E>): EntityInformation<E, ID> {
                return entityInformation as RequeryEntityInformation<E, ID>
            }
        }

        factory.setQueryLookupStrategyKey(QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND)
    }

    @Test
    fun `sutup basic instance correctly`() {
        assertThat(factory.getRepository(SimpleSampleRepository::class.java)).isNotNull
    }


    private interface SimpleSampleRepository: RequeryRepository<User, Int> {

        @Transactional
        override fun findById(id: Int): Optional<User>
    }

    interface SampleCustomRepository {

        fun throwingRuntimeException()

        @Throws(IOException::class)
        fun throwingCheckedException()
    }

    class SampleCustomRepositoryImpl: SampleCustomRepository {

        override fun throwingRuntimeException() {
            throw IllegalArgumentException("You lose!")
        }

        @Throws(IOException::class)
        override fun throwingCheckedException() {
            throw IOException("You lose!")
        }
    }

    private interface SampleRepository: RequeryRepository<User, Int>, SampleCustomRepository {

        fun findByEmail(email: String): User = User().also { it.email = email }

        fun customMethod(id: Long?): User = User()
    }

    class CustomRequeryRepository<T: Any, ID: Any>(
        entityInformation: RequeryEntityInformation<T, ID>,
        operations: RequeryOperations)
        : SimpleRequeryRepository<T, ID>(entityInformation, operations)
}