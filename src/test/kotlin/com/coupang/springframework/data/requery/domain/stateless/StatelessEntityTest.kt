package com.coupang.springframework.data.requery.domain.stateless

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

/**
 * StatelessEntityTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class StatelessEntityTest: AbstractDomainTest() {

    @Test
    fun `insert and delete stateless`() {

        val uuid = UUID.randomUUID()

        val entity = StatelessEntity().also {
            it.id = uuid.toString()
            it.flag1 = true
            it.flag2 = false
        }

        requeryTmpl.insert(entity)

        val found = requeryTmpl.findById(StatelessEntity::class.java, entity.id)!!
        assertThat(found.id).isEqualTo(entity.id)
    }
}