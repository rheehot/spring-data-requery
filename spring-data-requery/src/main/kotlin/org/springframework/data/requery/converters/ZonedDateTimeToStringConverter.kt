package org.springframework.data.requery.converters

import io.requery.Converter
import java.time.ZonedDateTime

/**
 * Convert [ZonedDateTime] to [String]
 *
 * @author debop
 * @since 18. 7. 2
 */
class ZonedDateTimeToStringConverter: Converter<ZonedDateTime, String> {

    override fun getPersistedType(): Class<String> = String::class.java
    override fun getMappedType(): Class<ZonedDateTime> = ZonedDateTime::class.java
    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out ZonedDateTime>?, value: String?): ZonedDateTime? {
        return value?.let { ZonedDateTime.parse(it) }
    }

    override fun convertToPersisted(value: ZonedDateTime?): String? {
        return value?.toIsoZonedDateTimeString()
    }

}