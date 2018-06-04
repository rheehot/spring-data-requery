package com.coupang.springframework.data.requery.domain.time

import com.coupang.springframework.data.requery.domain.AbstractPersistable
import io.requery.*
import io.requery.converter.*
import java.time.*
import java.util.*

/**
 * AbstractTimeEvent
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
@Entity
abstract class AbstractTimeEvent: AbstractPersistable<UUID>() {

    @get:Key
    abstract override var id: UUID?

    abstract var name: String

    @get:Convert(LocalDateConverter::class)
    abstract var localDate: LocalDate

    @get:Convert(LocalDateTimeConverter::class)
    abstract var localDateTime: LocalDateTime

    // BUG: LocalTime 은 milliseconds 값을 가지는데, java.sql.Time 은 seconds 까지 밖에 표현하지 못한다 (DB마다 다르다)
    @get:Column(name = "local_time")
    @get:Convert(LocalTimeConverter::class)
    abstract var localTime: LocalTime

    @get:Convert(OffsetDateTimeConverter::class)
    abstract var offsetDateTime: OffsetDateTime

    @get:Convert(ZonedDateTimeConverter::class)
    abstract var zonedDateTime: ZonedDateTime


    @PreInsert
    @PreUpdate
    fun onPreUpsert() {
        localDate = LocalDate.now()
        localDateTime = LocalDateTime.now()
        localTime = LocalTime.now()

        offsetDateTime = OffsetDateTime.now()
        zonedDateTime = ZonedDateTime.now()
    }
}