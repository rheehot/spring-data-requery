package org.springframework.data.requery.kotlin.domain

import io.requery.sql.KotlinEntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.kotlin.AbstractRequeryTest
import org.springframework.data.requery.kotlin.configs.RequeryTestConfiguration
import org.springframework.data.requery.kotlin.core.RequeryOperations
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
        val rnd = Random(System.currentTimeMillis())
    }

    @Autowired lateinit var kotlinDataStore: KotlinEntityDataStore<Any>
    @Autowired lateinit var operations: RequeryOperations

    @Test
    fun `context loading`() {
        assertThat(kotlinDataStore).isNotNull
        assertThat(operations).isNotNull
    }
}