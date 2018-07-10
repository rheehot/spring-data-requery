package org.springframework.data.requery.kotlin.domain.superclass

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.superclass.model.DerivedAEntity
import org.springframework.data.requery.kotlin.domain.superclass.model.DerivedBEntity
import org.springframework.data.requery.kotlin.domain.superclass.model.RelatedEntity

/**
 * org.springframework.data.requery.kotlin.domain.superclass.InheritanceTest
 *
 * @author debop
 */
class InheritanceTest: AbstractDomainTest() {

    @Test
    fun `create derived class`() {

        val related = RelatedEntity()
        operations.insert(related)

        val derivedA = DerivedAEntity()
        derivedA.relateds.add(related)
        operations.insert(derivedA)

        val derivedB = DerivedBEntity().apply { flag = false }
        operations.upsert(derivedB)

        val loaded = operations.findById(DerivedAEntity::class, derivedA.id)
        assertThat(loaded).isNotNull
        assertThat(loaded?.id).isEqualTo(derivedA.id)
        assertThat(loaded?.relateds?.firstOrNull()?.id).isEqualTo(related.id)
    }
}
