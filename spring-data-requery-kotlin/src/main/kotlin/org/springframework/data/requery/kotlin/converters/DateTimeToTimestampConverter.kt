package org.springframework.data.requery.kotlin.converters

import io.requery.Converter
import org.joda.time.DateTime
import java.sql.Timestamp

/**
 * Convert [DateTime] to [Timestamp]
 *
 * @author debop
 * @since 18. 7. 2
 */
class DateTimeToTimestampConverter: Converter<DateTime, Timestamp> {

    override fun getPersistedType(): Class<Timestamp> = Timestamp::class.java
    override fun getMappedType(): Class<DateTime> = DateTime::class.java
    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out DateTime>?, value: Timestamp?): DateTime? {
        return value?.let { DateTime(it.time) }
    }

    override fun convertToPersisted(value: DateTime?): Timestamp? {
        return value?.let { Timestamp(it.millis) }
    }


}