package org.springframework.data.requery.domain.model2;

import io.requery.Entity;
import io.requery.Key;
import io.requery.Table;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.requery.domain.AbstractPersistable;
import org.springframework.data.requery.domain.ToStringBuilder;

import java.time.*;
import java.util.Objects;
import java.util.UUID;

/**
 * com.coupang.springframework.data.requery.domain.model2.AbstractModel2Event
 *
 * @author debop
 * @since 18. 6. 4
 */
@Getter
@Setter
@Entity
@Table(name = "model2_event")
public abstract class AbstractModel2Event extends AbstractPersistable<UUID> {

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
        return Objects.hash(name);
    }

    @Override
    protected @NotNull ToStringBuilder buildStringHelper() {
        return super.buildStringHelper()
            .add("name", name);
    }
}
