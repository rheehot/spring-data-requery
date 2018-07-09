package org.springframework.data.requery.kotlin.domain.stateless

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import java.time.LocalDateTime
import java.util.*

/**
 * org.springframework.data.requery.kotlin.domain.stateless.StatelessTest
 *
 * @author debop
 */
class StatelessTest: AbstractDomainTest() {

    @Test
    fun `insert and delete stateless`() {

        val uuid = UUID.randomUUID()

        val entry = EntryEntity().apply {
            id = uuid.toString()
            flag1 = true
            flag2 = false
            createdAt = LocalDateTime.now()
        }

        operations.insert(entry)

        val found = operations.findById(Entry::class, entry.id)!!
        assertThat(found.id).isEqualTo(entry.id)
    }
}