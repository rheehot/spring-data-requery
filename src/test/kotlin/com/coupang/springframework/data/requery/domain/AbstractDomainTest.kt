package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.AbstractSpringDataRequeryTest
import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration
import com.coupang.springframework.data.requery.core.RequeryTemplate
import io.requery.Persistable
import io.requery.sql.EntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

/**
 * AbstractDomainTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [RequeryTestConfiguration::class])
abstract class AbstractDomainTest: AbstractSpringDataRequeryTest() {

    companion object: KLogging()


    @Inject lateinit var dataStore: EntityDataStore<Persistable>
    @Inject lateinit var requeryTemplate: RequeryTemplate

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(requeryTemplate).isNotNull
    }

}