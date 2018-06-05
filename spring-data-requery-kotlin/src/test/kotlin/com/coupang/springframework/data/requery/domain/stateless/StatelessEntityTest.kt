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
        with(requeryKotlin) {
            val uuid = UUID.randomUUID()

            val entity = StatelessEntity().also {
                it.id = uuid.toString()
                it.flag1 = true
                it.flag2 = false
            }

            insert(entity)

            val found = findById(StatelessEntity::class, entity.id)!!
            assertThat(found.id).isEqualTo(entity.id)
        }
    }
}