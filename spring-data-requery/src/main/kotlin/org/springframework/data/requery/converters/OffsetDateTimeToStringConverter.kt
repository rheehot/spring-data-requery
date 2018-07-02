package org.springframework.data.requery.converters

import io.requery.Converter
import java.time.OffsetDateTime

/**
 * Convert [OffsetDateTime] to [String]
 * @author debop
 * @since 18. 7. 2
 */
class OffsetDateTimeToStringConverter: Converter<OffsetDateTime, String> {

    override fun getPersistedType(): Class<String> = String::class.java
    override fun getMappedType(): Class<OffsetDateTime> = OffsetDateTime::class.java
    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out OffsetDateTime>?, value: String?): OffsetDateTime? {
        return value?.let { OffsetDateTime.parse(it) }
    }

    override fun convertToPersisted(value: OffsetDateTime?): String? {
        return value?.toIsoOffsetDateTimeString()
    }
}