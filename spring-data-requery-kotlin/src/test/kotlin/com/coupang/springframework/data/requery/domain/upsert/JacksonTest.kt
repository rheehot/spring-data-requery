package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import io.requery.jackson.EntityMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.util.*

/**
 * com.coupang.springframework.data.requery.domain.upsert.JacksonTest
 * @author debop
 * @since 18. 6. 2
 */
@Ignore("Jackson을 사용하여 Deserialize하려면 Kotlin Interface가 이닌 Java Class로 association을 맺어야하는데, 이렇게하면 kapt generation을 수행할 수 없다.")
class JacksonTest: AbstractDomainTest() {

    private val mapper: EntityMapper by lazy {
        EntityMapper(Models.DEFAULT, requeryTemplate.dataStore)
    }

    @Before
    fun setup() {
        requeryTemplate.deleteAll(UpsertTag::class.java)
        requeryTemplate.deleteAll(UpsertEvent::class.java)
        requeryTemplate.deleteAll(UpsertPlace::class.java)
        requeryTemplate.deleteAll(UpsertLocation::class.java)
    }

    @Test
    fun `one to many jackson serialize`() {
        with(requeryTemplate) {
            val uuid = UUID.randomUUID()
            val event = UpsertEvent()
            event.id = uuid
            event.name = "test"

            val tag1 = UpsertTag().apply { id = UUID.randomUUID() }
            val tag2 = UpsertTag().apply { id = UUID.randomUUID() }

            event.tags.add(tag1)
            event.tags.add(tag2)

            val place = UpsertPlace().apply {
                id = "SF"
                name = "San Francisco, CA"
            }
            event.place = place

            insert(event)

            val jsonText = mapper.writeValueAsString(event)
            assertThat(jsonText).isNotEmpty()
            println(jsonText)

            val converted = mapper.readValue(jsonText, UpsertEvent::class.java)
            assertThat(converted).isEqualTo(event)
            assertThat(converted.place).isEqualTo(place)
            assertThat(converted.tags).hasSize(2).containsOnly(tag1, tag2)
        }
    }
}