package com.coupang.springframework.data.requery.configs

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.AbstractSpringDataRequeryTest
import com.coupang.springframework.data.requery.core.RequeryOperations
import com.coupang.springframework.data.requery.domain.sample.Person
import io.requery.Persistable
import io.requery.sql.EntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

/**
 * RequeryConfigurationTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 30
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [RequeryTestConfiguration::class])
class RequeryConfigurationTest: AbstractSpringDataRequeryTest() {

    companion object: KLogging()

    @Inject lateinit var dataStore: EntityDataStore<Persistable>
    @Inject lateinit var requeryTemplate: RequeryOperations

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(requeryTemplate).isNotNull
    }

    @Test
    fun `open database`() {
        val person = Person().apply { name = "person" }
        dataStore.insert(person)
        log.debug { "person.id=${person.id}" }
        assertThat(person.id).isNotNull()
    }
}