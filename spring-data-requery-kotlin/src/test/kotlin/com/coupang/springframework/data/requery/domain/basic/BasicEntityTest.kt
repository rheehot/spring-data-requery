package com.coupang.springframework.data.requery.domain.basic

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomLocation
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUser
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUsers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class BasicEntityTest: AbstractDomainTest() {

    @Before
    fun setup() {
        with(requeryKotlin) {
            deleteAll(BasicUser::class)
            deleteAll(BasicLocation::class)
            deleteAll(BasicGroup::class)
        }
    }

    @Test
    fun `insert user without association`() {
        with(requeryKotlin) {
            val user = randomUser()

            insert(user)
            assertThat(user.id).isNotNull()

            val loaded = findById(BasicUser::class, user.id)
            assertThat(loaded).isEqualTo(user)
        }
    }

    @Test
    fun `select with limit`() {
        with(requeryKotlin) {
            val user = randomUser()
            insert(user)

            // HINT: 특정 컬럼만 가지고 온 후, 다른 컬럼을 참조하면, Lazy loading을 수행해준다.
            val result =
                select(BasicUser::class, BasicUser.ID, BasicUser.NAME)
                    .limit(10)
                    .get()

            val first = result.first()
            assertThat(first.id).isEqualTo(user.id)
            assertThat(first.name).isEqualTo(user.name)

            // HINT: Lazy loading을 수행합니다 !!!
            assertThat(first.age).isEqualTo(user.age)
        }
    }

    @Test
    fun `select partial column as tuple`() {
        with(requeryKotlin) {
            val user = randomUser()
            insert(user)

            val result = select(BasicUser.ID, BasicUser.NAME).limit(10).get()
            result.use { rows ->
                val row = rows.first()
                assertThat(row.get(BasicUser.ID)).isEqualTo(user.id)
                assertThat(row.get(BasicUser.NAME)).isEqualTo(user.name)
            }
        }
    }

    @Test
    fun `run with transaction`() {
        with(requeryKotlin) {
            withTransaction {
                insert(randomUser())
                insert(randomUser())
                insert(randomUser())

                val result = select(BasicUser::class).limit(10).get()
                assertThat(result.toList()).hasSize(3)
            }
        }
    }

    @Test
    fun `query compound conditions`() {
        with(requeryKotlin) {
            val user1 = randomUser().apply { age = 75 }
            insert(user1)

            val user2 = randomUser().apply { age = 10; name = "Carol" }
            insert(user2)

            val user3 = randomUser().apply { age = 0; name = "Bob" }
            insert(user3)

            val result = select(BasicUser::class)
                .where((BasicUser.AGE gt 5) and (BasicUser.AGE lt 75) and (BasicUser.NAME ne "Bob"))
                .or(BasicUser.NAME eq "Bob")
                .get()

            assertThat(result.toList()).hasSize(2).containsOnly(user2, user3)
        }
    }

    @Test
    fun `query with join and order by`() {
        with(requeryKotlin) {
            val user = randomUser()
            user.location = randomLocation()

            insert(user)

            // select a.locationId, a.city, a.countryCode, a.line1, a.line2, a.line3, a.state, a.zip
            // from BasicLocation a inner join BasicUser b on b.location = a.locationId
            // where b.userId = ?
            // order by a.city desc 
            val result = select(BasicLocation::class)
                .join(BasicUser::class).on(BasicUser.LOCATION_ID.eq(BasicLocation.ID))
                .where(BasicUser.ID eq user.id)
                .orderBy(BasicLocation.CITY.desc())
                .get()

            assertThat(result.toList()).hasSize(1)
        }
    }

    @Test
    fun `query raw entity`() {
        with(requeryKotlin) {
            val user = randomUser()
            insert(user)

            val result = raw(BasicUser::class, "select * from BasicUser")
            assertThat(result.first()).isEqualTo(user)
        }
    }

    @Test
    fun `update query`() {
        with(requeryKotlin) {
            val user = randomUser().apply { age = 100 }
            insert(user)

            insertAll(randomUsers(10))

            val rowCount = update(BasicUser::class)
                .set(BasicUser.AGE, 50)
                .set(BasicUser.ABOUT, "nothing")
                .where(BasicUser.AGE eq 100)
                .get()
                .value()
                .toInt()

            assertThat(rowCount).isEqualTo(1)
        }
    }
}