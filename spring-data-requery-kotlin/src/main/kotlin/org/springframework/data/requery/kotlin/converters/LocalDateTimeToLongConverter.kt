package org.springframework.data.requery.kotlin.converters

import io.requery.Converter
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Convert [LocalDateTime] to [Long]
 * @author debop
 * @since 18. 7. 2
 */
class LocalDateTimeToLongConverter: Converter<LocalDateTime, Long> {

    override fun getPersistedType(): Class<Long> = Long::class.java
    override fun getMappedType(): Class<LocalDateTime> = LocalDateTime::class.java
    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out LocalDateTime>?, value: Long?): LocalDateTime? {
        return value?.let {
            val epochSecond = it / NANO_IN_SECONDS
            val nano = it % NANO_IN_SECONDS
            return LocalDateTime.ofEpochSecond(epochSecond, nano.toInt(), ZoneOffset.UTC)
        }
    }

    override fun convertToPersisted(value: LocalDateTime?): Long? {
        return value?.let {
            it.toEpochSecond(ZoneOffset.UTC) * NANO_IN_SECONDS + it.nano
        }
    }
}