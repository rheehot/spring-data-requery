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
        requeryTmpl.deleteAll(UpsertTag::class.java)
        requeryTmpl.deleteAll(UpsertEvent::class.java)
        requeryTmpl.deleteAll(UpsertPlace::class.java)
        requeryTmpl.deleteAll(UpsertLocation::class.java)
    }

    @Test
    fun `insert location with embedded address`() {
        val location = UpsertLocation().apply {
            name = "Tower 730"
            address.zipcode = "12345"
            address.city = "seoul"
        }
        requeryTmpl.insert(location)

        assertThat(location.isNew()).isFalse()

        val loaded = requeryTmpl.findById(UpsertLocation::class.java, location.id)!!
        assertThat(loaded).isEqualTo(location)
        assertThat(loaded.address.zipcode).isEqualTo("12345")
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

        event.addTag(tag1)
        event.addTag(tag2)

        requeryTmpl.insert(event)

        assertThat(requeryTmpl.count(UpsertEvent::class.java).get().value()).isEqualTo(1)
        assertThat(requeryTmpl.count(UpsertTag::class.java).get().value()).isEqualTo(2)

        val loaded = requeryTmpl.findById(UpsertEvent::class.java, event.id)!!

        assertThat(loaded.tags).hasSize(2).containsOnly(tag1, tag2)

        requeryTmpl.delete(loaded)

        assertThat(requeryTmpl.count(UpsertEvent::class.java).get().value()).isEqualTo(0)
        assertThat(requeryTmpl.count(UpsertTag::class.java).get().value()).isEqualTo(0)
    }

    @Test
    fun `upsert event`() {
        val eventId = UUID.randomUUID()
        val event = UpsertEvent().apply {
            id = eventId
            name = "test"
        }

        requeryTmpl.upsert(event)

        assertThat(requeryTmpl.count(UpsertEvent::class.java).get().value()).isEqualTo(1)

        val loaded = requeryTmpl.findById(UpsertEvent::class.java, event.id)!!
        assertThat(loaded).isEqualTo(event)

        requeryTmpl.delete(loaded)
        assertThat(requeryTmpl.count(UpsertEvent::class.java).get().value()).isEqualTo(0)
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

        place.addEvent(event)

        requeryTmpl.upsert(place)


        val savedPlace = requeryTmpl.findById(UpsertPlace::class.java, place.id)!!

        assertThat(savedPlace.id).isEqualTo(place.id)
        assertThat(savedPlace.events).hasSize(1)
        assertThat(savedPlace.events.firstOrNull()?.id).isEqualTo(eventId)
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

        event.addTag(tag1)
        event.addTag(tag2)

        requeryTmpl.upsert(event)

        assertThat(requeryTmpl.count(UpsertEvent::class.java).get().value()).isEqualTo(1)
        assertThat(requeryTmpl.count(UpsertTag::class.java).get().value()).isEqualTo(2)

        val loaded = requeryTmpl.findById(UpsertEvent::class.java, event.id)!!

        assertThat(loaded.tags).hasSize(2).containsOnly(tag1, tag2)

        requeryTmpl.delete(loaded)

        assertThat(requeryTmpl.count(UpsertEvent::class.java).get().value()).isEqualTo(0)
        assertThat(requeryTmpl.count(UpsertTag::class.java).get().value()).isEqualTo(0)
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

        place.addEvent(event)

        requeryTmpl.insert(place)


        val savedPlace = requeryTmpl.findById(UpsertPlace::class.java, place.id)!!

        assertThat(savedPlace.id).isEqualTo(place.id)
        assertThat(savedPlace.events).hasSize(1)
        assertThat(savedPlace.events.firstOrNull()?.id).isEqualTo(eventId)
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
        place.addEvent(event)
        place.events.clear()

        requeryTmpl.upsert(place)

        val savedPlace = requeryTmpl.findById(UpsertPlace::class.java, place.id)!!

        assertThat(savedPlace.id).isEqualTo(place.id)
        assertThat(savedPlace.events).hasSize(0)
    }

    @Test
    fun `upsert exists entity`() {
        val eventId = UUID.randomUUID()
        val event1 = UpsertEvent().apply {
            id = eventId
            name = "event1"
        }

        requeryTmpl.insert(event1)

        val event2 = UpsertEvent().apply {
            id = eventId
            name = "event2"
        }

        requeryTmpl.upsert(event2)

        val events = requeryTmpl.findAll(UpsertEvent::class.java)
        assertThat(events).hasSize(1).containsOnly(event2)
    }
}