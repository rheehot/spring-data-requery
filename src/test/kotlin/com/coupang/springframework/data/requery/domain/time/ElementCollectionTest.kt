package com.coupang.springframework.data.requery.domain.time

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import java.util.*

/**
 * ElementCollectionTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class ElementCollectionTest: AbstractDomainTest() {

    companion object: KLogging()

    @Test
    fun `insert element collection`() {

        assertThatThrownBy {
            val user = ElementCollectionUser().apply {
                id = UUID.randomUUID()
                name = "user"

                phoneNumbers = mutableSetOf("555-5555", "666-6666")
            }

            requeryTmpl.insert(user)


            val saved = dataStore
                .select(ElementCollectionUser::class.java)
                .where(ElementCollectionUser.ID.eq(user.id).and(ElementCollectionUser.NAME.eq(user.name)))
                .get()
                .first()

            assertThat(saved).isNotNull

            assertThat(saved.phoneNumbers).hasSize(2).containsOnly("555-5555", "666-6666")

        }.isInstanceOf(ClassCastException::class.java)
    }
}