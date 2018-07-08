package org.springframework.data.requery.converters;

import io.requery.Converter;
import org.joda.time.DateTime;

import javax.annotation.Nullable;
import java.sql.Timestamp;

/**
 * Convert joda-time {@link DateTime} to {@link Timestamp}
 *
 * @author debop
 */
public class DateTimeToTimestampConverter implements Converter<DateTime, Timestamp> {

    @Override
    public Class<DateTime> getMappedType() {
        return DateTime.class;
    }

    @Override
    public Class<Timestamp> getPersistedType() {
        return Timestamp.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public Timestamp convertToPersisted(DateTime value) {
        return (value != null) ? new Timestamp(value.getMillis()) : null;
    }

    @Override
    public DateTime convertToMapped(Class<? extends DateTime> type, @Nullable Timestamp value) {
        return (value != null) ? new DateTime(value.getTime()) : null;
    }
}
