package org.springframework.data.requery.domain

import io.requery.sql.EntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.AbstractRequeryTest
import org.springframework.data.requery.configs.RequeryTestConfiguration
import org.springframework.data.requery.core.RequeryOperations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

/**
 * AbstractDomainTest
 *
 * @author debop@coupang.com
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [RequeryTestConfiguration::class])
abstract class AbstractDomainTest: AbstractRequeryTest() {

    companion object {
        private val rnd = Random(System.currentTimeMillis())
    }

    @Autowired lateinit var dataStore: EntityDataStore<Any>
    @Autowired lateinit var requeryTemplate: RequeryOperations

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(requeryTemplate).isNotNull
    }
}