package com.coupang.springframework.data.requery.domain.time;

import com.coupang.springframework.data.requery.domain.AbstractDomainTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Diego on 2018. 6. 12..
 */
@Slf4j
public class JavaTimeConverterTest extends AbstractDomainTest {

    @Test
    public void insert_and_read_JSR310_time_with_converter() {
        UUID eventId = UUID.randomUUID();

        TimeEvent event = new TimeEvent();
        event.setId(eventId);
        event.setName("event");

        requeryTemplate.insert(event);

        TimeEvent loaded = requeryTemplate.findById(TimeEvent.class, eventId);

        assertThat(loaded.getId()).isEqualTo(eventId);
        assertThat(loaded.getName()).isEqualTo(event.getName());
        assertThat(loaded.getLocalDate()).isEqualTo(event.getLocalDate());
        assertThat(loaded.getLocalDateTime()).isEqualTo(event.getLocalDateTime());

        // BUG: LocalTime 은 milliseconds 값을 가지는데, java.sql.Time 은 seconds 까지 밖에 표현하지 못한다 (DB마다 다르다)
        assertThat(loaded.getLocalTime()).isEqualTo(event.getLocalTime().truncatedTo(ChronoUnit.SECONDS));

        assertThat(loaded.getOffsetDateTime()).isEqualTo(event.getOffsetDateTime());
        assertThat(loaded.getZonedDateTime()).isEqualTo(event.getZonedDateTime());
    }
}
