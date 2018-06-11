package com.coupang.springframework.data.requery.domain.upsert

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * UpsertTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class UpsertTest: AbstractDomainTest() {

    @Before
    fun setup() {
        with(requeryKotlin) {
            deleteAll(UpsertTag::class)
            deleteAll(UpsertEvent::class)
            deleteAll(UpsertPlace::class)
            deleteAll(UpsertLocation::class)
        }
    }

    @Test
    fun `insert location with embedded address`() {
        val location = UpsertLocation().apply {
            name = "Tower 730"
            address.zipcode = "12345"
            address.city = "seoul"
        }

        with(requeryKotlin) {
            insert(location)

            assertThat(location.isNew()).isFalse()

            val loaded = findById(UpsertLocation::class, location.id)!!
            assertThat(loaded).isEqualTo(location)
            assertThat(loaded.address.zipcode).isEqualTo("12345")
        }
    }

    @Test
    fun `insert many to many`() {
        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }

        val tag1 = UpsertTag().apply { id = UUID.randomUUID(); name = "tag1" }
        val tag2 = UpsertTag().apply { id = UUID.randomUUID(); name = "tag2" }

        event.tags.add(tag1)
        event.tags.add(tag2)

        with(requeryKotlin) {

            insert(event)

            assertThat(count(UpsertEvent::class).get().value()).isEqualTo(1)
            assertThat(count(UpsertTag::class).get().value()).isEqualTo(2)

            val loaded = findById(UpsertEvent::class, event.id)!!

            assertThat(loaded.tags).hasSize(2).containsOnly(tag1, tag2)

            delete(loaded)
            refreshAll(loaded)

            assertThat(count(UpsertEvent::class).get().value()).isEqualTo(0)
            assertThat(count(UpsertTag::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `upsert event`() {
        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }
        with(requeryKotlin) {
            upsert(event)

            assertThat(count(UpsertEvent::class).get().value()).isEqualTo(1)

            val loaded = findById(UpsertEvent::class, event.id)!!
            assertThat(loaded).isEqualTo(event)

            delete(loaded)
            assertThat(count(UpsertEvent::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `upsert one to many`() {
        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }

        val place = UpsertPlace().apply {
            id = UUID.randomUUID().toString()
            name = "place"
        }

        place.events.add(event)

        with(requeryKotlin) {

            upsert(place)
            val savedPlace = findById(UpsertPlace::class, place.id)!!

            assertThat(savedPlace.id).isEqualTo(place.id)
            assertThat(savedPlace.events).hasSize(1)
            assertThat(savedPlace.events.firstOrNull()?.id).isEqualTo(eventId)
        }
    }

    @Test
    fun `upsert many to many`() {
        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }

        val tag1 = UpsertTag().apply { id = UUID.randomUUID(); name = "tag1" }
        val tag2 = UpsertTag().apply { id = UUID.randomUUID(); name = "tag2" }

        event.tags.add(tag1)
        event.tags.add(tag2)

        with(requeryKotlin) {

            upsert(event)

            assertThat(count(UpsertEvent::class).get().value()).isEqualTo(1)
            assertThat(count(UpsertTag::class).get().value()).isEqualTo(2)

            val loaded = findById(UpsertEvent::class, event.id)!!

            assertThat(loaded.tags).hasSize(2).containsOnly(tag1, tag2)

            delete(loaded)

            assertThat(count(UpsertEvent::class).get().value()).isEqualTo(0)
            assertThat(count(UpsertTag::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `upsert insert one to many`() {

        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }

        val place = UpsertPlace().apply {
            id = UUID.randomUUID().toString()
            name = "place"
        }

        place.events.add(event)

        with(requeryKotlin) {

            insert(place)


            val savedPlace = findById(UpsertPlace::class, place.id)!!

            assertThat(savedPlace.id).isEqualTo(place.id)
            assertThat(savedPlace.events).hasSize(1)
            assertThat(savedPlace.events.firstOrNull()?.id).isEqualTo(eventId)
        }
    }

    @Test
    fun `upsert one to many with empty collection`() {
        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }

        val place = UpsertPlace().apply {
            id = UUID.randomUUID().toString()
            name = "place"
        }
        place.events.add(event)
        place.events.remove(event)

        with(requeryKotlin) {

            upsert(place)
            val savedPlace = findById(UpsertPlace::class, place.id)!!

            assertThat(savedPlace.id).isEqualTo(place.id)
            assertThat(savedPlace.events).hasSize(0)
        }
    }

    @Test
    fun `upsert exists entity`() {
        val eventId = UUID.randomUUID()
        val event1 = UpsertEvent().apply {
            id = eventId
            name = "event1"
        }

        with(requeryKotlin) {
            insert(event1)

            val event2 = UpsertEvent().apply {
                id = eventId
                name = "event2"
            }

            upsert(event2)

            val events = findAll(UpsertEvent::class)
            assertThat(events).hasSize(1).containsOnly(event2)
        }
    }
}