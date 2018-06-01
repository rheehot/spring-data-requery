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

    val random = Random(System.currentTimeMillis())

    val firstNames = arrayOf("Alice", "Bob", "Carol")
    val lastNames = arrayOf("Smith", "Lee", "Jones")

    fun randomPerson(): FuncPerson {
        return FuncPerson().apply {
            name = firstNames[random.nextInt(firstNames.size)] + " " + lastNames[random.nextInt(lastNames.size)]
            email = name.replace(" ", ".").toLowerCase() + "@example.com"
            uuid = UUID.randomUUID()
            homepage = URL("http://www.requery.io")
            birthday = LocalDate.of(1900 + random.nextInt(90), random.nextInt(11) + 1, random.nextInt(20) + 1)
        }
    }

    fun randomPersons(count: Int): MutableSet<FuncPerson> {
        return List(count) { randomPerson() }.toMutableSet()
    }

    fun randomAddress(): FuncAddress {
        return FuncAddress().apply {
            line1 = "${random.nextInt(4)} Fake St"
            city = "San Franciso"
            state = "CA"
            zip = (10000 + random.nextInt(70000)).toString()
            type = AddressType.HOME
            coordinate.latitude = 0.0F
            coordinate.longitude = 0.0F
        }
    }

    fun randomPhone(): FuncPhone {
        return FuncPhone().apply {
            phoneNumber = "555-${1000 + random.nextInt(8000)}"
            normalized = true
            extensions = IntArray(5) { 300 + it }
        }
    }
}