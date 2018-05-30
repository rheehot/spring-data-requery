package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.AbstractSpringDataRequeryTest
import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration
import com.coupang.springframework.data.requery.core.RequeryTemplate
import com.coupang.springframework.data.requery.domain.sample.CustomAbstractPersistable
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
 * com.coupang.springframework.data.requery.domain.MappingTest
 * @author debop
 * @since 18. 5. 30
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [RequeryTestConfiguration::class])
class MappingTest: AbstractSpringDataRequeryTest() {

    companion object: KLogging()

    @Inject lateinit var dataStore: EntityDataStore<Persistable>
    @Inject lateinit var requeryTemplate: RequeryTemplate

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(requeryTemplate).isNotNull
    }

    @Test
    fun `verify mapping entities`() {
        val person = Person().apply { name = "person" }
        requeryTemplate.insert(person)

        assertThat(person.id).isNotNull()
    }

    @Test
    fun `entity has only id`() {
        val custom = CustomAbstractPersistable()
        requeryTemplate.insert(custom)

        assertThat(custom.id).isNotNull()
    }
}