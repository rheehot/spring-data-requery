package org.springframework.data.requery.kotlin.repository.query

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
import org.springframework.data.requery.kotlin.domain.sample.UserEntity
import org.springframework.data.requery.kotlin.unwrap

class QueryExampleBuilderTest: AbstractDomainTest() {

    @Before
    fun setup() {
        operations.deleteAll(User::class)
    }

    private fun userQueryByExample(example: Example<UserEntity>): QueryElement<out Result<UserEntity>> {
        val base = operations.select(UserEntity::class).unwrap()
        return QueryByExampleBuilder.build(base, example)
    }

    @Test
    fun `firstname equals example`() {

        val user = UserEntity().apply { firstname = "example"; emailAddress = "debop@example.com" }
        operations.insert(user)
        assertThat(user.id).isNotNull().isGreaterThan(0)

        val exampleUser = UserEntity().apply { firstname = user.firstname }
        val example = Example.of(exampleUser)

        val query = userQueryByExample(example)

        val foundUser = query.get().firstOrNull()
        assertThat(foundUser).isNotNull
        assertThat(foundUser.id).isEqualTo(user.id)
    }

    @Test
    fun `firstname and email equals example`() {

        val user = UserEntity().apply { firstname = "example"; emailAddress = "debop@example.com" }
        operations.insert(user)
        assertThat(user.id).isNotNull().isGreaterThan(0)

        val exampleUser = UserEntity().apply { firstname = user.firstname; emailAddress = user.emailAddress }
        val example = Example.of(exampleUser)

        val query = userQueryByExample(example)

        val foundUser = query.get().firstOrNull()
        assertThat(foundUser).isNotNull
        assertThat(foundUser.id).isEqualTo(user.id)
    }

    @Test
    fun `firstname startsWith example`() {

        val user = UserEntity().apply { firstname = "example"; emailAddress = "debop@example.com" }
        operations.insert(user)
        assertThat(user.id).isNotNull().isGreaterThan(0)

        val exampleUser = UserEntity().apply { firstname = "EXA" }

        val example = Example.of(exampleUser,
                                 matching()
                                     .withMatcher("firstname", startsWith().ignoreCase())
                                     .withIgnoreNullValues()
                                     .withIgnorePaths("lastname", "emailAddress", "roles"))

        val query = userQueryByExample(example)

        val foundUser = query.get().firstOrNull()
        assertThat(foundUser).isNotNull
        assertThat(foundUser.id).isEqualTo(user.id)
    }
}