package com.coupang.springframework.data.requery.domain.time;

import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.*;
import io.requery.converter.*;
import lombok.Getter;

import java.time.*;
import java.util.UUID;

/**
 * @author Diego on 2018. 6. 12..
 */
@Getter
@Entity
public abstract class AbstractTimeEvent extends AbstractPersistable<UUID> {

    @Key
    protected UUID id;

    protected String name;

    @Convert(LocalDateConverter.class)
    protected LocalDate localDate;

    @Convert(LocalDateTimeConverter.class)
    protected LocalDateTime localDateTime;

    @Column(name = "local_time")
    @Convert(LocalTimeConverter.class)
    protected LocalTime localTime;

    @Convert(OffsetDateTimeConverter.class)
    protected OffsetDateTime offsetDateTime;

    @Convert(ZonedDateTimeConverter.class)
    protected ZonedDateTime zonedDateTime;

    @PreInsert
    @PreUpdate
    public void onPreUpsert() {
        localDate = LocalDate.now();
        localDateTime = LocalDateTime.now();
        localTime = LocalTime.now();

        offsetDateTime = OffsetDateTime.now();
        zonedDateTime = ZonedDateTime.now();
    }
}
