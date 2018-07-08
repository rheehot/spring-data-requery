package org.springframework.data.requery.converters;

import io.requery.Converter;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.time.ZonedDateTime;

/**
 * org.springframework.data.requery.converters.ZonedDateTimeToStringConverter
 *
 * @author debop
 */
public class ZonedDateTimeToStringConverter implements Converter<ZonedDateTime, String> {

    @Override
    public Class<ZonedDateTime> getMappedType() {
        return ZonedDateTime.class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(ZonedDateTime value) {
        return (value != null)
               ? Java8Times.toIsoZonedDateTimeString(value)
               : null;
    }

    @Override
    public ZonedDateTime convertToMapped(Class<? extends ZonedDateTime> type, @Nullable String value) {
        return (StringUtils.hasText(value))
               ? ZonedDateTime.parse(value)
               : null;
    }
}
