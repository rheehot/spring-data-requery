package org.springframework.data.requery.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.domain.sample.User

/**
 * org.springframework.data.requery.domain.MappingTest
 *
 * @author debop
 */
class MappingTest: AbstractDomainTest() {

    @Test
    fun `verify mapping entities`() {
        val user = User().apply {
            firstname = "Debop"
            lastname = "Bae"
            emailAddress = "debop@coupang.com"
        }

        requeryTemplate.insert(user)

        assertThat(user.id).isNotNull()
    }

    @Test
    fun `entity has only id, fail to generate requery entity`() {

        // Can't test because fail to generate requery entity
    }
}