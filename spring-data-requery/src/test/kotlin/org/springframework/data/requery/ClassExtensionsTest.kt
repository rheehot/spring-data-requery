package org.springframework.data.requery

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.domain.sample.*

class ClassExtensionsTest: AbstractRequeryTest() {

    @Test
    fun `is requery entity class`() {

        assertThat(User::class.java.isRequeryEntity).isFalse()
        assertThat(AbstractUser::class.java.isRequeryEntity).isTrue()

        assertThat(Role::class.java.isRequeryEntity).isFalse()
        assertThat(AbstractRole::class.java.isRequeryEntity).isTrue()
    }

    @Test
    fun `retrieve key property from User entity`() {

        val keyExpr = User::class.java.getKeyExpression<Int>()

        assertThat(keyExpr).isNotNull
        assertThat(keyExpr.name).isEqualTo("id")
        assertThat(keyExpr.classType).isEqualTo(Integer::class.java)

        val keyExpr2 = User::class.java.getKeyExpression<Int>()
        assertThat(keyExpr2).isEqualTo(keyExpr)
    }

    @Test
    fun `retrieve key property from Role entity`() {

        val keyExpr = Role::class.java.getKeyExpression<Int>()

        assertThat(keyExpr).isNotNull
        assertThat(keyExpr.name).isEqualTo("id")
        assertThat(keyExpr.classType).isEqualTo(Integer::class.java)

        val keyExpr2 = Role::class.java.getKeyExpression<Int>()
        assertThat(keyExpr2).isEqualTo(keyExpr)

        val accountsKeyExpr = Accounts::class.java.getKeyExpression<Long>()
        assertThat(accountsKeyExpr).isNotEqualTo(keyExpr)
    }
}