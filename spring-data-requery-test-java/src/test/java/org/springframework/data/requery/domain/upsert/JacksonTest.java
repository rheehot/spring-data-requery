package org.springframework.data.requery.domain.upsert;

import io.requery.jackson.EntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.requery.domain.AbstractDomainTest;
import org.springframework.data.requery.domain.Models;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Diego on 2018. 6. 10..
 */
@Slf4j
public class JacksonTest extends AbstractDomainTest {

    private EntityMapper entityMapper;

    @Before
    public void setup() {
        entityMapper = new EntityMapper(Models.DEFAULT, requeryTemplate.getDataStore());

        requeryTemplate.deleteAll(UpsertTag.class);
        requeryTemplate.deleteAll(UpsertEvent.class);
        requeryTemplate.deleteAll(UpsertPlace.class);
        requeryTemplate.deleteAll(UpsertLocation.class);
    }

    @Ignore("StackOverflowError")
    @Test
    public void one_to_many_jackson_serialize() throws Exception {
        UUID uuid = UUID.randomUUID();
        UpsertEvent event = new UpsertEvent();
        event.setId(uuid);
        event.setName("test");

        UpsertTag tag1 = new UpsertTag();
        tag1.setId(UUID.randomUUID());
        UpsertTag tag2 = new UpsertTag();
        tag2.setId(UUID.randomUUID());

        event.getTags().add(tag1);
        event.getTags().add(tag2);

        UpsertPlace place = new UpsertPlace();
        place.setId("SF");
        place.setName("San Francisco, CA");

        event.setPlace(place);

        requeryTemplate.insert(event);

        String jsonText = entityMapper.writeValueAsString(event);
        assertThat(jsonText).isNotEmpty();
        log.info(jsonText);

        UpsertEvent converted = entityMapper.readValue(jsonText, UpsertEvent.class);
        assertThat(converted).isEqualTo(event);
        assertThat(converted.getPlace()).isEqualTo(place);
        assertThat(converted.getTags()).hasSize(2).containsOnly(tag1, tag2);

    }
}
