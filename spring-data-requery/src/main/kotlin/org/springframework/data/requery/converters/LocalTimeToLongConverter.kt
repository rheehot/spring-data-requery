package org.springframework.data.requery.converters

import io.requery.Converter
import java.time.LocalTime

/**
 * org.springframework.data.requery.converters.LocalTimeToLongConverter
 * @author debop
 * @since 18. 7. 2
 */
class LocalTimeToLongConverter: Converter<LocalTime, Long> {

    override fun getPersistedType(): Class<Long> = Long::class.java
    override fun getMappedType(): Class<LocalTime> = LocalTime::class.java
    override fun getPersistedSize(): Int? = null

    override fun convertToMapped(type: Class<out LocalTime>?, value: Long?): LocalTime? {
        return value?.let { LocalTime.ofNanoOfDay(it) }
    }

    override fun convertToPersisted(value: LocalTime?): Long? {
        return value?.toNanoOfDay()
    }


}