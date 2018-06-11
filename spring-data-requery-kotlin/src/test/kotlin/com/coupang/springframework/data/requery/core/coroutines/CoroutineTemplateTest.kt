package com.coupang.springframework.data.requery.core.coroutines

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.basic.BasicGroup
import com.coupang.springframework.data.requery.domain.basic.BasicLocation
import com.coupang.springframework.data.requery.domain.basic.BasicUser
import com.coupang.springframework.data.requery.domain.basic.RandomData
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUser
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUsers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

/**
 * com.coupang.springframework.data.requery.core.coroutines.CoroutineTemplateTest
 * @author debop
 * @since 18. 6. 2
 */
class CoroutineTemplateTest: AbstractDomainTest() {

    private val coroutineTemplate by lazy { CoroutineRequeryTemplate(kotlinDataStore) }

    @Before
    fun setup() {
        runBlocking {
            with(coroutineTemplate) {
                val result1 = async { deleteAll(BasicLocation::class) }
                val result2 = async { deleteAll(BasicGroup::class) }
                val result3 = async { deleteAll(BasicUser::class) }

                result1.await()
                result2.await()
                result3.await()
            }
        }
    }

    @Test
    fun `async insert`() {
        val user = randomUser()
        runBlocking {
            with(coroutineTemplate) {
                val savedUser = async { insert(user) }

                val loaded = async {
                    select(BasicUser::class).where(BasicUser.ID.eq(savedUser.await().id)).get().firstOrNull()
                }
                assertThat(loaded.await()).isEqualTo(user)
            }
        }
    }

    @Test
    fun `async insert and count`() {
        runBlocking {
            with(coroutineTemplate) {
                val user = randomUser()

                val count = async {
                    insert(user)

                    assertThat(user.id).isNotNull()
                    count(BasicUser::class).get().value()
                }

                assertThat(count.await()).isEqualTo(1)
            }
        }
    }

    @Test
    fun `async insert one to many`() = runBlocking<Unit> {
        with(coroutineTemplate) {
            val user = randomUser()

            async { insert(user) }.await()
            assertThat(user.id).isNotNull()

            val group = RandomData.randomGroup()
            group.members.add(user)

            async { insert(group) }.await()

            assertThat(user.groups).hasSize(1)
            assertThat(group.members).hasSize(1)
        }
    }

    @Test
    fun `async query udpate`() = runBlocking<Unit> {
        val user = randomUser().apply { age = 100 }

        with(coroutineTemplate) {
            async { insert(user) }.await()
            async { insert(randomUser().apply { age = 10 }) }.await()

            val affectedCount = async {
                update(BasicUser::class)
                    .set(BasicUser.ABOUT, "nothing")
                    .set(BasicUser.AGE, 50)
                    .where(BasicUser.AGE eq user.age)
                    .get()
                    .value()
            }

            assertThat(affectedCount.await()).isEqualTo(1)
        }
    }

    @Test
    fun `query stream`() = runBlocking<Unit> {
        val users = randomUsers(100)

        with(coroutineTemplate) {
            async { insertAll(users) }.await()

            val loadedUsers = async {
                select(BasicUser::class)
                    .orderBy(BasicUser.NAME.asc().nullsFirst())
                    .limit(200)
                    .get()
                    .stream()
            }

            assertThat(loadedUsers.await().count()).isEqualTo(100L)
        }
    }
}