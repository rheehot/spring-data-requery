package org.springframework.data.requery.kotlin

import io.requery.query.Operator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher.matching
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.sample.User
import org.springframework.data.requery.kotlin.domain.sample.UserEntity

/**
 * org.springframework.data.requery.ExampleExtensionsTest
 *
 * @author debop
 */
class ExampleExtensionsTest: AbstractDomainTest() {

    @Test
    fun `build whereclause from Example instance`() {

        val user = UserEntity().apply { lastname = "Bae" }

        val example = Example.of(user, matching().withIgnoreNullValues())

        val queryElement = example.buildQueryElement(operations, User::class)

        assertThat(queryElement).isNotNull
        assertThat(queryElement.whereElements).hasSize(1)

        // lastname = 'Bae'
        assertThat(queryElement.whereElements.find { it.condition.operator == Operator.EQUAL }).isNotNull
    }
}