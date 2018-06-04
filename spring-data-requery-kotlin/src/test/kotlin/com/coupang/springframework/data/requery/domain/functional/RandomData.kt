package com.coupang.springframework.data.requery.domain.functional

import java.net.URL
import java.time.LocalDate
import java.util.*

/**
 * RandomData
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
object RandomData {

    private val rnd = Random(System.currentTimeMillis())

    private val firstNames = arrayOf("Alice", "Bob", "Carol")
    private val lastNames = arrayOf("Smith", "Lee", "Jones")

    fun randomPerson(): FuncPerson {
        return FuncPerson().apply {
            name = firstNames[rnd.nextInt(firstNames.size)] + " " + lastNames[rnd.nextInt(lastNames.size)]
            email = name.replace(" ", ".").toLowerCase() + rnd.nextInt(1000).toString() + "@example.com"
            uuid = UUID.randomUUID()
            homepage = URL("http://www.requery.io")
            birthday = LocalDate.of(1900 + rnd.nextInt(90), rnd.nextInt(11) + 1, rnd.nextInt(20) + 1)
        }
    }

    fun randomPersons(count: Int): MutableSet<FuncPerson> {
        return List(count) { randomPerson() }.toMutableSet()
    }

    fun randomAddress(): FuncAddress {
        return FuncAddress().apply {
            line1 = "${rnd.nextInt(4)} Fake St"
            city = "San Franciso"
            state = "CA"
            zip = (10000 + rnd.nextInt(70000)).toString()
            type = AddressType.HOME
            coordinate.latitude = 0.0F
            coordinate.longitude = 0.0F
        }
    }

    fun randomPhone(): FuncPhone {
        return FuncPhone().apply {
            phoneNumber = "555-${1000 + rnd.nextInt(8000)}"
            normalized = true
            extensions = IntArray(5) { 300 + it }
        }
    }
}