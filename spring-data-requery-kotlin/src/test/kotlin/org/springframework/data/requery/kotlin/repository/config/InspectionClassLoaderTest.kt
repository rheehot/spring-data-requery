package org.springframework.data.requery.repository.config

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InspectionClassLoaderTest {

    @Test
    fun `should load external class`() {

        val classLoader = InspectionClassLoader(this.javaClass.classLoader)

        val isolated = classLoader.loadClass("org.h2.Driver")
        val included = javaClass.classLoader.loadClass("org.h2.Driver")

        assertThat(isolated.classLoader)
            .isSameAs(classLoader)
            .isNotSameAs(javaClass.classLoader)

        assertThat(isolated).isNotEqualTo(included)
    }
}