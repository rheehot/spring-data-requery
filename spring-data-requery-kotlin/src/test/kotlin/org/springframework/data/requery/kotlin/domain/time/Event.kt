package org.springframework.data.requery.kotlin.domain.time


import io.requery.*
import org.springframework.data.requery.kotlin.converters.LocalDateTimeToLongConverter
import org.springframework.data.requery.kotlin.converters.LocalTimeToLongConverter
import org.springframework.data.requery.kotlin.converters.OffsetDateTimeToStringConverter
import org.springframework.data.requery.kotlin.converters.ZonedDateTimeToStringConverter
import java.time.*
import java.util.*

/**
 * UpsertEvent
 *
 * @author debop@coupang.com
 * @since 18. 5. 14
 */
@Entity(name = "TimedEvent")
@Table(name = "TimedEvent")
interface Event: Persistable {

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