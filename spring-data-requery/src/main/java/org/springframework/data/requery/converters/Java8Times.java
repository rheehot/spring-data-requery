package org.springframework.data.requery.converters;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * org.springframework.data.requery.converters.Java8Times
 *
 * @author debop
 */
@UtilityClass
public class Java8Times {

    public static long NANO_IN_SECONDS = 1_000_000_000L;

    @NotNull
    public static String toIsoOffsetDateTimeString(@NotNull TemporalAccessor accessor) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(accessor);
    }

    @NotNull
    public static String toIsoOffsetDateString(@NotNull TemporalAccessor accessor) {
        return DateTimeFormatter.ISO_OFFSET_DATE.format(accessor);
    }

    @NotNull
    public static String toIsoOffsetTimeString(@NotNull TemporalAccessor accessor) {
        return DateTimeFormatter.ISO_OFFSET_TIME.format(accessor);
    }

    @NotNull
    public static String toIsoZonedDateTimeString(@NotNull TemporalAccessor accessor) {
        return DateTimeFormatter.ISO_ZONED_DATE_TIME.format(accessor);
    }
}
