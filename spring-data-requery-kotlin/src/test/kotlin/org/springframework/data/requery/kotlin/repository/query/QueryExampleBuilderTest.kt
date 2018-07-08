package org.springframework.data.requery.repository.query

import io.requery.query.Result
import io.requery.query.element.QueryElement
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith
import org.springframework.data.domain.ExampleMatcher.matching
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.sample.User

class QueryExampleBuilderTest: AbstractDomainTest() {

    @Before
    fun setup() {
        requeryTemplate.deleteAll(User::class.java)
    }

    private fun userQueryByExample(example: Example<User>): QueryElement<out Result<User>> {
        val base = requeryTemplate.select(User::class.java).unwrap()
        return QueryByExampleBuilder.getWhereAndOr(base, example)
    }

    @Test
    fun `firstname equals example`() {

        val user = User().apply { firstname = "example"; emailAddress = "debop@example.com" }
        requeryTemplate.insert(user)

        val exampleUser = User().apply { firstname = user.firstname }
        val example = Example.of(exampleUser)

        val query = userQueryByExample(example)

        val foundUser = query.get().firstOrNull()
        assertThat(foundUser).isNotNull.isEqualTo(user)
    }

    @Test
    fun `firstname and email equals example`() {

        val user = User().apply { firstname = "example"; emailAddress = "debop@example.com" }
        requeryTemplate.insert(user)

        val exampleUser = User().apply { firstname = user.firstname; emailAddress = user.emailAddress }
        val example = Example.of(exampleUser)

        val query = userQueryByExample(example)

        val foundUser = query.get().firstOrNull()
        assertThat(foundUser).isNotNull.isEqualTo(user)
    }

    @Test
    fun `firstname startsWith example`() {

        val user = User().apply { firstname = "example"; emailAddress = "debop@example.com" }
        requeryTemplate.insert(user)

        val exampleUser = User().apply { firstname = "EXA" }

        val example = Example.of(exampleUser,
                                 matching()
                                     .withMatcher("firstname", startsWith().ignoreCase())
                                     .withIgnoreNullValues()
                                     .withIgnorePaths("lastname", "emailAddress"))

        val query = userQueryByExample(example)

        val foundUser = query.get().firstOrNull()
        assertThat(foundUser).isNotNull.isEqualTo(user)
    }
}