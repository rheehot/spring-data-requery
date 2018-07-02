package org.springframework.data.requery.converters

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

/**
 * org.springframework.data.requery.converters.Java8Times
 * @author debop
 * @since 18. 7. 2
 */


fun TemporalAccessor.toIsoOffsetDateTimeString(): String = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(this)

fun TemporalAccessor.toIsoOffsetDateString(): String = DateTimeFormatter.ISO_OFFSET_DATE.format(this)

fun TemporalAccessor.toIsoOffsetTimeString(): String = DateTimeFormatter.ISO_OFFSET_TIME.format(this)

fun TemporalAccessor.toIsoZonedDateTimeString(): String = DateTimeFormatter.ISO_ZONED_DATE_TIME.format(this)