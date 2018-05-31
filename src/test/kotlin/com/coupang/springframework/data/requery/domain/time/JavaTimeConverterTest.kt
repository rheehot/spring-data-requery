package com.coupang.springframework.data.requery.domain.time

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
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

        requeryTemplate.insert(event)

        val loaded = requeryTemplate.findById(TimeEvent::class.java, eventId)!!

        assertThat(loaded.id).isEqualTo(eventId)
        assertThat(loaded.name).isEqualTo(event.name)
        assertThat(loaded.localDate).isEqualTo(event.localDate)
        assertThat(loaded.localDateTime).isEqualTo(event.localDateTime)
        assertThat(loaded.localTime).isEqualTo(event.localTime)
        assertThat(loaded.offsetDateTime).isEqualTo(event.offsetDateTime)
        assertThat(loaded.zonedDateTime).isEqualTo(event.zonedDateTime)
    }
}