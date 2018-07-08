package org.springframework.data.requery.kotlin.configs

import io.requery.meta.EntityModel
import io.requery.sql.EntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.requery.kotlin.AbstractRequeryTest
import org.springframework.data.requery.kotlin.core.RequeryOperations
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

/**
 * RequeryConfigurationTest
 *
 * @author debop@coupang.com
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [RequeryTestConfiguration::class])
class RequeryConfigurationTest: AbstractRequeryTest() {

    @Autowired
    lateinit var dataStore: EntityDataStore<Any>

    @Autowired
    lateinit var requeryTemplate: RequeryOperations

    @Autowired
    lateinit var entityModel: EntityModel

    @Test
    fun `load entity model`() {
        log.info { "EntityModel=$entityModel" }
        assertThat(entityModel).isNotNull
    }

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(requeryTemplate).isNotNull
    }
}