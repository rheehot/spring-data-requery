package com.coupang.springframework.data.requery.domain.upsert;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Diego on 2018. 6. 10..
 */
public class UpsertTest extends AbstractDomainTest {

    @Before
    public void setup() {
        requeryTemplate.deleteAll(UpsertTag.class);
        requeryTemplate.deleteAll(UpsertEvent.class);
        requeryTemplate.deleteAll(UpsertPlace.class);
        requeryTemplate.deleteAll(UpsertLocation.class);
    }

    @Test
    public void insert_location_with_embedded_address() {
        UpsertLocation location = new UpsertLocation();
        location.setName("Tower 730");
        location.getAddress().setZipcode("12345");
        location.getAddress().setCity("seoul");

        requeryTemplate.insert(location);

        assertThat(location.isNew()).isFalse();

        UpsertLocation loaded = requeryTemplate.findById(UpsertLocation.class, location.getId());
        assertThat(loaded).isEqualTo(location);
        assertThat(loaded.getAddress().getZipcode()).isEqualTo("12345");
    }

    @Test
    public void insert_many_to_many() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("test");

        UpsertTag tag1 = new UpsertTag();
        tag1.setId(UUID.randomUUID());
        tag1.setName("tag1");
        UpsertTag tag2 = new UpsertTag();
        tag2.setId(UUID.randomUUID());
        tag2.setName("tag2");

        event.getTags().add(tag1);
        event.getTags().add(tag2);

        requeryTemplate.insert(event);

        assertThat(requeryTemplate.count(UpsertEvent.class).get().value()).isEqualTo(1);
        assertThat(requeryTemplate.count(UpsertTag.class).get().value()).isEqualTo(2);

        UpsertEvent loaded = requeryTemplate.findById(UpsertEvent.class, event.getId());

        assertThat(loaded.getTags()).hasSize(2).containsOnly(tag1, tag2);

        requeryTemplate.delete(loaded);
        requeryTemplate.refreshAll(loaded);

        assertThat(requeryTemplate.count(UpsertEvent.class).get().value()).isEqualTo(0);
        assertThat(requeryTemplate.count(UpsertTag.class).get().value()).isEqualTo(0);
    }

    @Test
    public void upsert_event() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("test");

        requeryTemplate.upsert(event);

        assertThat(requeryTemplate.count(UpsertEvent.class).get().value()).isEqualTo(1);

        UpsertEvent loaded = requeryTemplate.findById(UpsertEvent.class, event.getId());
        assertThat(loaded).isEqualTo(event);

        requeryTemplate.delete(loaded);
        assertThat(requeryTemplate.count(UpsertEvent.class).get().value()).isEqualTo(0);
    }

    @Test
    public void upsert_one_to_many() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("test");

        UpsertPlace place = new UpsertPlace();
        place.setId(UUID.randomUUID().toString());
        place.setName("place");

        place.getEvents().add(event);

        requeryTemplate.upsert(place);
        UpsertPlace savedPlace = requeryTemplate.findById(UpsertPlace.class, place.getId());

        assertThat(savedPlace.getId()).isEqualTo(place.getId());
        assertThat(savedPlace.getEvents()).hasSize(1);
        assertThat(savedPlace.getEvents().iterator().next().getId()).isEqualTo(eventId);
    }

    @Test
    public void upsert_many_to_many() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("test");

        UpsertTag tag1 = new UpsertTag();
        tag1.setId(UUID.randomUUID());
        tag1.setName("tag1");
        UpsertTag tag2 = new UpsertTag();
        tag2.setId(UUID.randomUUID());
        tag2.setName("tag2");

        event.getTags().add(tag1);
        event.getTags().add(tag2);

        requeryTemplate.upsert(event);
        assertThat(requeryTemplate.count(UpsertEvent.class).get().value()).isEqualTo(1);
        assertThat(requeryTemplate.count(UpsertTag.class).get().value()).isEqualTo(2);

        UpsertEvent loaded = requeryTemplate.findById(UpsertEvent.class, event.getId());

        requeryTemplate.delete(loaded);

        assertThat(requeryTemplate.count(UpsertEvent.class).get().value()).isEqualTo(0);
        assertThat(requeryTemplate.count(UpsertTag.class).get().value()).isEqualTo(0);
    }

    @Test
    public void upsert_insert_one_to_many() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("test");

        UpsertPlace place = new UpsertPlace();
        place.setId(UUID.randomUUID().toString());
        place.setName("place");

        place.getEvents().add(event);

        requeryTemplate.insert(place);

        UpsertPlace savedPlace = requeryTemplate.findById(UpsertPlace.class, place.getId());

        assertThat(savedPlace.getId()).isEqualTo(place.getId());
        assertThat(savedPlace.getEvents()).hasSize(1);
        assertThat(savedPlace.getEvents().iterator().next().getId()).isEqualTo(eventId);
    }

    @Test
    public void upsert_one_to_many_with_empty_collection() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("test");

        UpsertPlace place = new UpsertPlace();
        place.setId(UUID.randomUUID().toString());

        place.getEvents().add(event);
        place.getEvents().remove(event);

        requeryTemplate.upsert(place);
        UpsertPlace savedPlace = requeryTemplate.findById(UpsertPlace.class, place.getId());

        assertThat(savedPlace.getId()).isEqualTo(place.getId());
        assertThat(savedPlace.getEvents()).hasSize(0);
    }

    @Test
    public void upsert_exists_entity() {
        UUID eventId = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(eventId);
        event.setName("event1");

        requeryTemplate.insert(event);

        UpsertEvent event2 = new UpsertEvent();
        event2.setId(eventId);
        event2.setName("event2");

        requeryTemplate.upsert(event2);

        Iterable<UpsertEvent> events = requeryTemplate.findAll(UpsertEvent.class);
        assertThat(events).hasSize(1).containsOnly(event2);
    }

}
