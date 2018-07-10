package org.springframework.data.requery.kotlin.domain

import mu.KotlinLogging
import org.springframework.data.requery.kotlin.domain.basic.BasicGroupEntity
import org.springframework.data.requery.kotlin.domain.basic.BasicLocationEntity
import org.springframework.data.requery.kotlin.domain.basic.BasicUserEntity
import org.springframework.data.requery.kotlin.domain.sample.UserEntity
import java.time.LocalDate
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

    fun randomUser(): UserEntity {
        try {
            return UserEntity().apply {
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

    fun randomUsers(count: Int): Set<UserEntity> {
        val users = mutableSetOf<UserEntity>()
        var userCount = 0
        while(userCount < count) {
            val user = randomUser()
            if(users.add(user)) {
                userCount++
            }
        }
        return users
    }

    fun randomBasicUser(): BasicUserEntity {
        try {
            return BasicUserEntity().apply {
                name = firstnames[rnd.nextInt(firstnames.size)] + " " + lastnames[rnd.nextInt(lastnames.size)]
                email = "${name.replace(" ", ".")}@${companies[rnd.nextInt(companies.size)]}.org"
                birthday = LocalDate.of(1900 + rnd.nextInt(90), rnd.nextInt(11) + 1, rnd.nextInt(27) + 1)
            }
        } catch(e: Exception) {
            log.warn(e) { "Fail to create BasicUser" }
            throw e
        }
    }

    fun randomBasicUsers(count: Int): Set<BasicUserEntity> {
        val users = hashSetOf<BasicUserEntity>()
        var userCount = 0

        while(userCount < count) {
            if(users.add(randomBasicUser())) {
                userCount++
            }
        }
        return users
    }

    fun randomBasicLocation(): BasicLocationEntity {

        return BasicLocationEntity().apply {
            line1 = rnd.nextInt(4).toString() + " Fake St."
            city = "Seoul"
            state = "Seoul"
            countryCode = "KR"
            zip = (10000 + rnd.nextInt(89999)).toString()
        }
    }

    fun randomBasicGroup(): BasicGroupEntity {

        return BasicGroupEntity().apply {
            name = "group ${rnd.nextInt(1000)}"
            description = "description ${rnd.nextInt(1000)}"
        }
    }
}