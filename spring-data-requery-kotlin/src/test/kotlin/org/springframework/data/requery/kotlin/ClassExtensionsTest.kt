package org.springframework.data.requery.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.sample.*

class ClassExtensionsTest: AbstractRequeryTest() {

    @Test
    fun `is requery entity class`() {

        assertThat(UserEntity::class.java.isRequeryEntity).isFalse()
        assertThat(User::class.java.isRequeryEntity).isTrue()

        assertThat(RoleEntity::class.java.isRequeryEntity).isFalse()
        assertThat(Role::class.java.isRequeryEntity).isTrue()
    }

    @Test
    fun `find requery entity`() {

        assertThat(UserEntity::class.java.findRequeryEntity()).isEqualTo(User::class.java)
        assertThat(RoleEntity::class.java.findRequeryEntity()).isEqualTo(Role::class.java)
    }

    @Test
    fun `retrieve key property from User entity`() {

        val keyExpr = User::class.java.getKeyExpression<Int>()

        assertThat(keyExpr).isNotNull
        assertThat(keyExpr.name).isEqualTo("id")
        assertThat(keyExpr.classType).isEqualTo(Integer::class.java)

        val keyExpr2 = UserEntity::class.java.getKeyExpression<Int>()
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

    @Test
    fun `field is associated`() {

        // Kotlin 에서는 @get:Annotation 이므로 get이 붙어야 합니다.
        val method = User::class.java.getDeclaredMethod("getRoles")
        log.debug { "Method=$method" }
        log.debug { "Declared Annotations size=${method.declaredAnnotations.size}" }
        log.debug { "Annotations size=${method.annotations.size}" }

        assertThat(method.isAnnotationPresent(io.requery.ManyToMany::class.java)).isTrue()

        val methods = User::class.java.findEntityMethods()

        log.debug { "methods size=${methods.size}" }
        methods.forEach {
            log.debug { "method=$it" }
            log.debug { "method is requery entity method = ${it.isRequeryEntityMethod()}" }
            log.debug { "method is requery generated method = ${it.isRequeryGeneratedMethod()}" }
        }
    }

    @Test
    fun `is annotated property`() {

        val methods = UserEntity::class.java.findEntityMethods()

        methods.forEach { m ->
            log.debug {
                """
                    | "method name=${m.name}"
                    |    is Key = ${m.isKeyAnnoatedElement()}
                    |    is Assoicated = ${m.isAssociatedAnnotatedElement()}
                    |    is Embedded = ${m.isEmbeddedAnnoatedElement()}
                    |    is Transient = ${m.isTransientAnnotatedElement()}
                """.trimMargin()
            }
        }
    }
}