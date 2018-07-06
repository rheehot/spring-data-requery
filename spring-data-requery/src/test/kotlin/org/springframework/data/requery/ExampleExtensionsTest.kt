package org.springframework.data.requery

import io.requery.query.Operator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher.matching
import org.springframework.data.requery.domain.AbstractDomainTest
import org.springframework.data.requery.domain.sample.User

/**
 * org.springframework.data.requery.ExampleExtensionsTest
 *
 * @author debop
 */
class ExampleExtensionsTest: AbstractDomainTest() {

    @Test
    fun `build whereclause from Example instance`() {

        val user = User().apply { lastname = "Bae" }

        val example = Example.of(user, matching().withIgnoreNullValues())

        val queryElement = example.buildQueryElement(requeryTemplate, User::class.java)

        assertThat(queryElement).isNotNull
        assertThat(queryElement.whereElements).hasSize(1)

        // lastname = 'Bae'
        assertThat(queryElement.whereElements.find { it.condition.operator == Operator.EQUAL }).isNotNull
    }
}