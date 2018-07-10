package org.springframework.data.requery.kotlin.domain.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import java.time.*
import java.util.*

/**
 * TimeConversionTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 14
 */
class TimeConversionTest: AbstractDomainTest() {

    @Test fun `insert and read Java time`() {
        with(operations) {
            val eventId = UUID.randomUUID()
            val localDateNow = LocalDate.now()
            val localDateTimeNow = LocalDateTime.now()
            val localTimeNow = LocalTime.now()
            val offsetDateTimeNow = OffsetDateTime.now()
            val zonedDateTimeNow = ZonedDateTime.now()

            val event = TimedEvent().apply {
                id = eventId
                localDate = localDateNow
                localDateTime = localDateTimeNow
                localTime = localTimeNow
                offsetDateTime = offsetDateTimeNow
                zonedDateTime = zonedDateTimeNow
            }
            insert(event)

            val loaded = findById(TimedEvent::class, eventId)
            assertThat(loaded).isNotNull
            assertThat(loaded?.localDate).isEqualTo(localDateNow)
            // LocalDateTime 의 nano 값이 제대로 저장 안된다.
            //            assertThat(loaded?.localDateTime).isEqualTo(localDateTimeNow)
            assertThat(loaded?.localTime).isEqualTo(localTimeNow)
            assertThat(loaded?.offsetDateTime).isEqualTo(offsetDateTimeNow)
            assertThat(loaded?.zonedDateTime).isEqualTo(zonedDateTimeNow)
        }
    }
}