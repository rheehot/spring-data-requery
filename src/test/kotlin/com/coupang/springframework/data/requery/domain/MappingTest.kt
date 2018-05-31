package com.coupang.springframework.data.requery.domain

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.sample.CustomAbstractPersistable
import com.coupang.springframework.data.requery.domain.sample.Person
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * com.coupang.springframework.data.requery.domain.MappingTest
 * @author debop
 * @since 18. 5. 30
 */
class MappingTest: AbstractDomainTest() {

    companion object: KLogging()

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