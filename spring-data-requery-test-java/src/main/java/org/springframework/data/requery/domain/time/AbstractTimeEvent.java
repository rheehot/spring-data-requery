package org.springframework.data.requery.domain.time;

import io.requery.*;
import io.requery.converter.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.time.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Diego on 2018. 6. 12..
 */
@Getter
@Entity
public abstract class AbstractTimeEvent extends AbstractPersistable<UUID> {

    private static final long serialVersionUID = 8767142386985468901L;

    public AbstractTimeEvent() {}

    public AbstractTimeEvent(@NotNull UUID id) {
        this.id = id;
    }

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

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Transient
    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name)
            .add("localDate", localDate)
            .add("localTime", localTime)
            .add("localDateTime", localDateTime)
            .add("offsetDateTime", offsetDateTime)
            .add("zonedDateTime", zonedDateTime);
    }
}
