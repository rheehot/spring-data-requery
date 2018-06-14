package com.coupang.springframework.data.requery.configs

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.AbstractSpringDataRequeryTest
import com.coupang.springframework.data.requery.core.KotlinRequeryOperations
import com.coupang.springframework.data.requery.core.RequeryOperations
import io.requery.meta.EntityModel
import io.requery.sql.EntityDataStore
import io.requery.sql.KotlinEntityDataStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject
import javax.sql.DataSource

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [KotlinRequeryTestConfiguration::class])
class KotlinRequeryConfigurationTest: AbstractSpringDataRequeryTest() {

    companion object: KLogging()

    @Inject lateinit var dataSource: DataSource

    @Inject lateinit var entityModel: EntityModel

    @Inject lateinit var dataStore: EntityDataStore<Any>
    @Inject lateinit var kotlinDataStore: KotlinEntityDataStore<Any>
    @Inject lateinit var requeryTemplate: RequeryOperations
    @Inject lateinit var kotlinRequeryTemplate: KotlinRequeryOperations

    @Test
    fun `context loading`() {
        assertThat(dataSource).isNotNull

        assertThat(entityModel).isNotNull

        assertThat(dataStore).isNotNull
        assertThat(kotlinDataStore).isNotNull

        assertThat(requeryTemplate).isNotNull
        assertThat(kotlinRequeryTemplate).isNotNull
    }

    @Test
    fun `inject requery repository`() {

        // TODO: 구현 해야 함

    }
}