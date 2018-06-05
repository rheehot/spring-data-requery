package com.coupang.springframework.data.requery.domain.inheritance

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InheritanceTest: AbstractDomainTest() {

    @Test
    fun `create derived class`() {
        with(requeryKotlin) {
            val related = InheritanceRelated().apply { attribute = "attribute" }
            insert(related)

            val derivedA = InheritanceDerivedA().apply { relateds += related; attr = "attr" }
            insert(derivedA)

            val derivedB = InheritanceDerivedB().apply { relateds += related; flag = false }
            insert(derivedB)

            val loadedA = findById(InheritanceDerivedA::class, derivedA.id)!!
            assertThat(loadedA.id).isEqualTo(derivedA.id)
            assertThat(loadedA.relateds.firstOrNull()).isEqualTo(related)

            val loadedB = findById(InheritanceDerivedB::class, derivedB.id)!!
            assertThat(loadedB.id).isEqualTo(derivedB.id)
            assertThat(loadedB.relateds.firstOrNull()).isEqualTo(related)
        }
    }
}