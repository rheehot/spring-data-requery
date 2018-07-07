package org.springframework.data.requery.repository.query

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.domain.sample.AbstractUser
import org.springframework.data.requery.domain.sample.User

/**
 * org.springframework.data.requery.repository.query.DefaultRequeryEntityMetadataTest
 *
 * @author debop
 */
class DefaultRequeryEntityMetadataTest {

    class Foo {}

    @Test
    fun `return configured type`() {

        val metadata = DefaultRequeryEntityMetadata(Foo::class.java)
        assertThat(metadata.javaType).isEqualTo(Foo::class.java)
    }

    @Test
    fun `return simple class name as entity name by default`() {

        val metadata = DefaultRequeryEntityMetadata(Foo::class.java)
        assertThat(metadata.entityName).isEqualTo(Foo::class.java.simpleName)
    }

    @Test
    fun `return customized entity name if configured`() {

        val metadata = DefaultRequeryEntityMetadata(AbstractUser::class.java)
        assertThat(metadata.entityName).isEqualTo("User")

        val metadata2 = DefaultRequeryEntityMetadata(User::class.java)
        assertThat(metadata2.entityName).isEqualTo("User")
    }
}