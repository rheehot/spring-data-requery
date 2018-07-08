package org.springframework.data.requery.converters;

import io.requery.Converter;

import javax.annotation.Nullable;
import java.time.LocalTime;

/**
 * org.springframework.data.requery.converters.LocalTimeToLongConverter
 *
 * @author debop
 */
public class LocalTimeToLongConverter implements Converter<LocalTime, Long> {

    @Override
    public Class<LocalTime> getMappedType() {
        return LocalTime.class;
    }

    @Override
    public Class<Long> getPersistedType() {
        return Long.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return Long.SIZE;
    }

    @Override
    public Long convertToPersisted(LocalTime value) {
        return (value != null) ? value.toNanoOfDay() : null;
    }

    @Override
    public LocalTime convertToMapped(Class<? extends LocalTime> type, @Nullable Long value) {
        return (value != null) ? LocalTime.ofNanoOfDay(value) : null;
    }
}
