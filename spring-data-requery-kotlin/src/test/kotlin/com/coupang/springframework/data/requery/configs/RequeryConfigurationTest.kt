package com.coupang.springframework.data.requery.configs

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.AbstractSpringDataRequeryTest
import com.coupang.springframework.data.requery.core.RequeryOperations
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

    @Inject lateinit var dataStore: EntityDataStore<Any>
    @Inject lateinit var requeryTemplate: RequeryOperations

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(requeryTemplate).isNotNull
    }

    @Test
    fun `inject requery repository`() {

        // TODO: 구현 해야 함

    }
}