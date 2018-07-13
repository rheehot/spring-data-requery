package org.springframework.data.requery.kotlin.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.sample.UserEntity

/**
 * org.springframework.data.requery.kotlin.domain.MappingTest
 *
 * @author debop
 */
class MappingTest: AbstractDomainTest() {

    @Test
    fun `verify mapping entities`() {

        operations.deleteAll(UserEntity::class)

        val user = UserEntity().apply {
            firstname = "Debop"
            lastname = "Bae"
            emailAddress = "debop@coupang.com"
        }

        operations.insert(user)

        assertThat(user.id).isNotNull()
    }

    @Test
    fun `entity has only id, fail to generate requery entity`() {

        // Can't test because fail to generate requery entity
    }
}