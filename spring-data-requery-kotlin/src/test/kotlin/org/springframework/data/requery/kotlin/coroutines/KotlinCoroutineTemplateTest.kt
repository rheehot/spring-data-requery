package org.springframework.data.requery.kotlin.coroutines

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.RandomData
import org.springframework.data.requery.kotlin.domain.RandomData.randomBasicUser
import org.springframework.data.requery.kotlin.domain.basic.BasicGroup
import org.springframework.data.requery.kotlin.domain.basic.BasicLocation
import org.springframework.data.requery.kotlin.domain.basic.BasicUser
import org.springframework.data.requery.kotlin.domain.basic.BasicUserEntity

class KotlinCoroutineTemplateTest: AbstractDomainTest() {

    private val coroutineTemplate by lazy { KotlinCoroutineRequeryTemplate(kotlinDataStore) }

    @Before
    fun setup() {
        runBlocking {
            with(coroutineTemplate) {
                val result1 = async(Unconfined) { deleteAll(BasicLocation::class) }
                val result2 = async(Unconfined) { deleteAll(BasicGroup::class) }
                val result3 = async(Unconfined) { deleteAll(BasicUser::class) }

                result1.await()
                result2.await()
                result3.await()
            }
        }
    }

    @Test
    fun `async insert`() = runBlocking<Unit> {
        val user = randomBasicUser()
        with(coroutineTemplate) {
            val savedUser = insert(user)

            val loaded = async(Unconfined) {
                select(BasicUser::class).where(BasicUserEntity.ID.eq(savedUser.id)).get().firstOrNull()
            }
            assertThat(loaded.await()).isEqualTo(user)
        }
    }

    @Test
    fun `async insert and count`() = runBlocking<Unit> {
        with(coroutineTemplate) {
            val user = randomBasicUser()

            val count = async(Unconfined) {
                insert(user)
                assertThat(user.id).isNotNull()
                count(BasicUser::class).get().value()
            }

            assertThat(count.await()).isEqualTo(1)
        }
    }

    @Test
    fun `async insert one to many`() = runBlocking<Unit> {
        with(coroutineTemplate) {
            val user = randomBasicUser()

            withContext(Unconfined) { insert(user) }
            assertThat(user.id).isNotNull()

            val group = RandomData.randomBasicGroup()
            group.members.add(user)

            withContext(Unconfined) { insert(group) }

            assertThat(user.groups).hasSize(1)
            assertThat(group.members).hasSize(1)
        }
    }

    @Test
    fun `async query udpate`() = runBlocking<Unit> {
        val user = randomBasicUser().apply { age = 100 }

        with(coroutineTemplate) {
            withContext(Unconfined) {
                insert(user)
                insert(randomBasicUser().apply { age = 10 })
            }

            val affectedCount = async(Unconfined) {
                update(BasicUser::class)
                    .set(BasicUserEntity.ABOUT, "nothing")
                    .set(BasicUserEntity.AGE, 50)
                    .where(BasicUserEntity.AGE eq user.age)
                    .get()
                    .value()
            }

            val affectedCount2 = async(Unconfined) {
                update(BasicUser::class)
                    .set(BasicUserEntity.ABOUT, "twenty")
                    .set(BasicUserEntity.AGE, 20)
                    .where(BasicUserEntity.AGE eq 10)
                    .get()
                    .value()
            }

            assertThat(affectedCount.await()).isEqualTo(1)
            assertThat(affectedCount2.await()).isEqualTo(1)
        }
    }

    @Test
    fun `query stream`() = runBlocking<Unit> {
        val users = RandomData.randomBasicUsers(100)

        with(coroutineTemplate) {
            withContext(Unconfined) { insertAll(users) }

            val loadedUsers = async(Unconfined) {
                select(BasicUser::class)
                    .orderBy(BasicUserEntity.NAME.asc().nullsFirst())
                    .limit(200)
                    .get()
                    .stream()
            }

            assertThat(loadedUsers.await().count()).isEqualTo(100L)
        }
    }
}