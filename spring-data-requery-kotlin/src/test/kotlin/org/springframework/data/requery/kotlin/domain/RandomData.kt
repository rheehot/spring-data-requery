package org.springframework.data.requery.kotlin.domain

import mu.KotlinLogging
import org.springframework.data.requery.kotlin.domain.sample.User
import java.util.*

/**
 * org.springframework.data.requery.kotlin.domain.RandomData
 *
 * @author debop
 */
object RandomData {

    private val log = KotlinLogging.logger { }

    private val rnd = Random(System.currentTimeMillis())

    private val firstnames = arrayOf("Alice", "Bob", "Carol", "Debop", "Diego", "Jinie", "Nickoon", "Aiden")
    private val lastnames = arrayOf("Smith", "Lee", "Jones", "Bae", "Ahn", "Park", "Jeon", "Nam")
    private val companies = arrayOf("example", "coupang", "korea", "gmail")

    fun randomUser(): User {
        try {
            return User().apply {
                firstname = firstnames[rnd.nextInt(firstnames.size)]
                lastname = lastnames[rnd.nextInt(lastnames.size)]
                emailAddress = "$firstname.$lastname@${companies[rnd.nextInt(companies.size)]}.org"
                dateOfBirth = Calendar.Builder()
                    .setDate(1900 + rnd.nextInt(90), rnd.nextInt(11) + 1, rnd.nextInt(27) + 1)
                    .build()
                    .time
            }
        } catch(e: Exception) {
            log.warn(e) { "Fail to create user" }
            throw e
        }
    }

    fun randomUsers(count: Int): Set<User> {
        val users = mutableSetOf<User>()
        var userCount = 0
        while(userCount < count) {
            val user = randomUser()
            if(users.add(user)) {
                userCount++
            }
        }
        return users
    }
}