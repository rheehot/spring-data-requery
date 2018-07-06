package org.springframework.data.requery.mapping

import io.requery.Key
import io.requery.Version
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RequeryMappingContextTest {

    @Test
    fun `persistent entity rejects Spring Data @Version annotation`() {

        val context = RequeryMappingContext()
        val entity = context.getRequiredPersistentEntity(Sample::class.java)

        assertThat(entity).isNotNull

        assertThat(entity.getRequiredPersistentProperty("id").isIdProperty).isTrue()
        assertThat(entity.getRequiredPersistentProperty("springId").isIdProperty).isFalse()

        assertThat(entity.getRequiredPersistentProperty("version").isVersionProperty).isTrue()
        assertThat(entity.getRequiredPersistentProperty("springVersion").isVersionProperty).isFalse()
    }

    private class Sample {
        
        @get:Key
        val id: Long? = 0

        @org.springframework.data.annotation.Id
        var springId: Long = 0

        @Version
        var version: Long = 0

        @org.springframework.data.annotation.Version
        var springVersion: Long = 0
    }
}