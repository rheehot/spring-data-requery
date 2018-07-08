package org.springframework.data.requery.converters;

import io.requery.Converter;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;

/**
 * org.springframework.data.requery.converters.OffsetDateTimeToStringConverter
 *
 * @author debop
 */
public class OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {

    @Override
    public Class<OffsetDateTime> getMappedType() {
        return OffsetDateTime.class;
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
    public String convertToPersisted(OffsetDateTime value) {
        return (value != null)
               ? Java8Times.toIsoOffsetDateTimeString(value)
               : null;
    }

    @Override
    public OffsetDateTime convertToMapped(Class<? extends OffsetDateTime> type, @Nullable String value) {
        return (StringUtils.hasText(value))
               ? OffsetDateTime.parse(value)
               : null;
    }
}
