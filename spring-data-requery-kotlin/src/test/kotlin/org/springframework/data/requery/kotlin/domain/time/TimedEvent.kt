package org.springframework.data.requery.kotlin.domain.time


import io.requery.Column
import io.requery.Convert
import io.requery.Entity
import io.requery.Key
import org.springframework.data.requery.kotlin.converters.LocalDateTimeToLongConverter
import org.springframework.data.requery.kotlin.converters.LocalTimeToLongConverter
import org.springframework.data.requery.kotlin.converters.OffsetDateTimeToStringConverter
import org.springframework.data.requery.kotlin.converters.ZonedDateTimeToStringConverter
import org.springframework.data.requery.kotlin.domain.PersistableObject
import java.time.*
import java.util.*

/**
 * UpsertEvent
 *
 * @author debop@coupang.com
 * @since 18. 5. 14
 */
@Entity
interface TimedEvent: PersistableObject {

    @get:Key
    var id: UUID

    var name: String

    var localDate: LocalDate

    @get:Convert(LocalDateTimeToLongConverter::class)
    var localDateTime: LocalDateTime?

    @get:Column(name = "local_time")
    @get:Convert(LocalTimeToLongConverter::class)
    var localTime: LocalTime?

    @get:Convert(OffsetDateTimeToStringConverter::class)
    var offsetDateTime: OffsetDateTime?

    @get:Convert(ZonedDateTimeToStringConverter::class)
    var zonedDateTime: ZonedDateTime?

}