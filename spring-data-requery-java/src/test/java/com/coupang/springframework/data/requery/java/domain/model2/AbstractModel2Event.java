package com.coupang.springframework.data.requery.java.domain.model2;

import com.coupang.kotlinx.objectx.ToStringBuilder;
import com.coupang.springframework.data.requery.domain.AbstractPersistable;
import io.requery.Entity;
import io.requery.Key;
import io.requery.Table;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.util.UUID;

import static com.coupang.kotlinx.core.HashxKt.hashOf;

/**
 * com.coupang.springframework.data.requery.java.domain.model2.AbstractModel2Event
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Setter
@Entity
@Table(name = "model2_event")
public class AbstractModel2Event extends AbstractPersistable<UUID> {

    private static final long serialVersionUID = 3671180738904090633L;

    @Key
    protected UUID id;

    protected String name;

    protected LocalDate javaLocalDate;

    protected LocalDateTime javaLocalDateTime;

    protected LocalTime javaLocalTime;

    protected OffsetDateTime offsetDateTime;

    protected ZonedDateTime zonedDateTime;


    @Override
    public int hashCode() {
        return hashOf(name);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
