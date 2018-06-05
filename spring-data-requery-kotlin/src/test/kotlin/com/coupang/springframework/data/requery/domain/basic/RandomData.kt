package com.coupang.springframework.data.requery.domain.basic

import java.net.URL
import java.time.LocalDate
import java.util.*

/**
 * com.coupang.springframework.data.requery.domain.basic.RandomData
 * @author debop
 * @since 18. 6. 2
 */
object RandomData {

    private val rnd: Random = Random(System.currentTimeMillis())

    private val firstNames = arrayOf("Alice", "Bob", "Carol")
    private val lastNames = arrayOf("Smith", "Lee", "Jones")

    fun randomUser(): BasicUser {
        return BasicUser().apply {
            name = firstNames[rnd.nextInt(firstNames.size)] + " " + lastNames[rnd.nextInt(lastNames.size)]
            email = name.replace(" ", ".").toLowerCase() + rnd.nextInt(1000).toString() + "@example.com"
            uuid = UUID.randomUUID()
            homepage = URL("http://www.requery.io")
            birthday = LocalDate.of(1900 + rnd.nextInt(90), rnd.nextInt(11) + 1, rnd.nextInt(20) + 1)
        }
    }

    fun randomUsers(count: Int): MutableSet<BasicUser> {
        val set = HashSet<BasicUser>()
        while(set.size < count) {
            set.add(randomUser())
        }
        return set
    }

    fun randomLocation(): BasicLocation {
        return BasicLocation().apply {
            line1 = "${rnd.nextInt(4)} Fake St"
            city = "Seoul"
            state = "Seoul"
            zip = (10000 + rnd.nextInt(70000)).toString()
            countryCode = "KR"
        }
    }

    fun randomGroup(): BasicGroup {
        return BasicGroup().apply {
            name = "group ${rnd.nextInt(1000)}"
            description = "description ${rnd.nextInt(1000)}"
        }
    }
}