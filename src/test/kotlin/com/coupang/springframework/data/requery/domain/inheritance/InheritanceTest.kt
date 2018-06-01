package com.coupang.springframework.data.requery.domain.inheritance

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class InheritanceTest: AbstractDomainTest() {

    @Test
    fun `create derived class`() {
        val related = InheritanceRelated().apply { attribute = "attribute" }
        requeryTmpl.insert(related)

        val derivedA = InheritanceDerivedA().apply { relateds += related; attr = "attr" }
        requeryTmpl.insert(derivedA)

        val derivedB = InheritanceDerivedB().apply { relateds += related; flag = false }
        requeryTmpl.insert(derivedB)


        val loadedA = requeryTmpl.findById(InheritanceDerivedA::class.java, derivedA.id)!!
        assertThat(loadedA.id).isEqualTo(derivedA.id)
        assertThat(loadedA.relateds.firstOrNull()).isEqualTo(related)

        val loadedB = requeryTmpl.findById(InheritanceDerivedB::class.java, derivedB.id)!!
        assertThat(loadedB.id).isEqualTo(derivedB.id)
        assertThat(loadedB.relateds.firstOrNull()).isEqualTo(related)
    }
}