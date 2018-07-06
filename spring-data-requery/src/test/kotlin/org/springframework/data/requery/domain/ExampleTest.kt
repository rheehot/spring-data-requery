package org.springframework.data.requery.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers
import org.springframework.data.domain.ExampleMatcher.matching

/**
 * org.springframework.data.requery.domain.ExampleTest
 *
 * @author debop
 */
class ExampleTest {

    private data class Person(val firstname: String)

    private lateinit var person: Person
    private lateinit var example: Example<Person>

    @Before
    fun setup() {
        person = Person("rand")
        example = Example.of(person)
    }

    @Test
    fun `returns sample objects class as probetype`() {

        assertThat(example.probeType).isEqualTo(Person::class.java)

    }

    @Test
    fun `should compare using hashCode and equals`() {
        val example = Example.of(person, matching().withIgnoreCase("firstname"))
        val sameAsExample = Example.of(person, matching().withIgnoreCase("firstname"))

        val different = Example.of(person, matching().withMatcher("firstname", GenericPropertyMatchers.contains()))

        assertThat(example.hashCode()).isEqualTo(sameAsExample.hashCode())
        assertThat(example.hashCode()).isNotEqualTo(different.hashCode())

        assertThat(example).isEqualTo(sameAsExample)
        assertThat(example).isNotEqualTo(different)
    }
}