package com.coupang.springframework.data.requery.domain.time

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * JavaTimeConverterTest
 *
 * @author debop@coupang.com
 * @since 18. 5. 31
 */
class JavaTimeConverterTest: AbstractDomainTest() {

    companion object: KLogging()

    @Test
    fun `insert and read JSR-310 time with converter`() {
        val eventId = UUID.randomUUID()

        val event = TimeEvent().apply {
            id = eventId
            name = "event"
        }

        with(requeryKotlin) {

            insert(event)

            val loaded = findById(TimeEvent::class, eventId)!!

            assertThat(loaded.id).isEqualTo(eventId)
            assertThat(loaded.name).isEqualTo(event.name)
            assertThat(loaded.localDate).isEqualTo(event.localDate)
            assertThat(loaded.localDateTime).isEqualTo(event.localDateTime)

            // BUG: LocalTime 은 milliseconds 값을 가지는데, java.sql.Time 은 seconds 까지 밖에 표현하지 못한다 (DB마다 다르다)
            assertThat(loaded.localTime).isEqualTo(event.localTime.truncatedTo(ChronoUnit.SECONDS))

            assertThat(loaded.offsetDateTime).isEqualTo(event.offsetDateTime)
            assertThat(loaded.zonedDateTime).isEqualTo(event.zonedDateTime)
        }
    }
}