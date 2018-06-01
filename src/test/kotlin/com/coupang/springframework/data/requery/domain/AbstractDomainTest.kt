package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.AbstractSpringDataRequeryTest
import com.coupang.springframework.data.requery.configs.RequeryTestConfiguration
import com.coupang.springframework.data.requery.core.RequeryKotlinTemplate
import com.coupang.springframework.data.requery.core.RequeryTemplate
import io.requery.Persistable
import io.requery.sql.EntityDataStore
import io.requery.sql.KotlinEntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
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

    companion object: KLogging() {
        val rnd = Random(System.currentTimeMillis())
    }


    @Inject lateinit var dataStore: EntityDataStore<Persistable>
    @Inject lateinit var ktDataStore: KotlinEntityDataStore<Persistable>
    @Inject lateinit var requeryTmpl: RequeryTemplate
    @Inject lateinit var requeryKtTmpl: RequeryKotlinTemplate

    @Test
    fun `context loading`() {
        assertThat(dataStore).isNotNull
        assertThat(ktDataStore).isNotNull
        assertThat(requeryTmpl).isNotNull
        assertThat(requeryKtTmpl).isNotNull
    }

}