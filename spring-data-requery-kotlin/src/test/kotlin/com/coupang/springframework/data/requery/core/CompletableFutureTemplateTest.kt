package com.coupang.springframework.data.requery.core

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.basic.BasicGroup
import com.coupang.springframework.data.requery.domain.basic.BasicLocation
import com.coupang.springframework.data.requery.domain.basic.BasicUser
import com.coupang.springframework.data.requery.domain.basic.RandomData
import io.requery.async.KotlinCompletableEntityStore
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

/**
 * com.coupang.springframework.data.requery.core.CompletableFutureTemplateTest
 * @author debop
 * @since 18. 6. 2
 */
class CompletableFutureTemplateTest: AbstractDomainTest() {

    companion object: KLogging()

    private val asyncEntityStore: KotlinCompletableEntityStore<Any> by lazy {
        KotlinCompletableEntityStore(requeryKotlin.dataStore, ForkJoinPool.commonPool())
    }

    @Before
    fun setup() {
        with(requeryKotlin) {
            deleteAll(BasicLocation::class)
            deleteAll(BasicGroup::class)
            deleteAll(BasicUser::class)
        }
    }

    @Test
    fun `async insert`() {
        val user = RandomData.randomUser()
        asyncEntityStore.insert(user).thenAccept { u ->
            Assertions.assertThat(u.id).isNotNull()

            val loaded = asyncEntityStore.select(BasicUser::class).where(BasicUser.ID eq user.id).get().first()
            Assertions.assertThat(loaded).isEqualTo(user)
        }.join()
    }

    @Test
    fun `async insert and count`() {

        with(asyncEntityStore) {
            val user = RandomData.randomUser()

            insert(user)
                .thenAccept { savedUser ->
                    Assertions.assertThat(savedUser.id).isNotNull()
                }
                .thenCompose {
                    count(BasicUser::class).get().toCompletableFuture()
                }
                .get()
        }
    }

    @Test
    fun `async insert one to many`() {

        with(asyncEntityStore) {
            val user = RandomData.randomUser()

            insert(user)
                .thenApply { savedUser ->
                    val group = BasicGroup().apply { name = "group" }
                    group.members.add(savedUser)
                    group
                }
                .thenCompose { group ->
                    insert(group)
                }
                .get()

            Assertions.assertThat(user.groups).hasSize(1)
        }
    }

    @Test
    fun `async query update`() {
        val user = RandomData.randomUser().apply { age = 100 }

        with(asyncEntityStore) {
            insert(user)
                .thenCompose { _ ->
                    update<BasicUser>(BasicUser::class)
                        .set(BasicUser.ABOUT, "nothing")
                        .set(BasicUser.AGE, 50)
                        .where(BasicUser.AGE eq user.age)
                        .get()
                        .toCompletableFuture(Executors.newSingleThreadExecutor())
                }.thenApplyAsync { rowCount ->
                    Assertions.assertThat(rowCount).isEqualTo(1)
                }
                .get()
        }
    }

    @Test
    fun `async to blocking`() {
        val user = RandomData.randomUser()
        asyncEntityStore.toBlocking().insert(user)
        Assertions.assertThat(user.id).isNotNull()
    }

    @Test
    fun `query stream`() {
        val users = RandomData.randomUsers(100)

        with(asyncEntityStore) {
            insert<BasicUser>(users).join()

            val loadedUsers =
                select(BasicUser::class)
                    .orderBy(BasicUser.NAME.asc().nullsLast())
                    .limit(200)
                    .get()
                    .stream()
                    .map { user -> user }

            Assertions.assertThat(loadedUsers.count()).isEqualTo(100L)
        }
    }
}