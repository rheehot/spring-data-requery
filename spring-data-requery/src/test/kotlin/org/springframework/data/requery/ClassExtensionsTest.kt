package org.springframework.data.requery

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.domain.sample.AbstractUser

/**
 * ClassExtensionsTest
 *
 * @author debop@coupang.com
 */
class ClassExtensionsTest: AbstractRequeryTest() {

    @Test
    fun `retrieve key property from entity`() {

        val keyExpr = AbstractUser::class.java.getKeyExpression<Int>()

        assertThat(keyExpr).isNotNull
        assertThat(keyExpr.name).isEqualToIgnoringCase("id")
        assertThat(keyExpr.classType).isEqualTo(Integer::class.java)
    }
}