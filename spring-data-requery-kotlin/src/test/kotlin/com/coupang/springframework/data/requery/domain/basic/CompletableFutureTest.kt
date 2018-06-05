package com.coupang.springframework.data.requery.domain.basic

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUser
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUsers
import io.requery.async.KotlinCompletableEntityStore
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

/**
 * com.coupang.springframework.data.requery.domain.basic.CompletableFutureTest
 * @author debop
 * @since 18. 6. 2
 */
class CompletableFutureTest: AbstractDomainTest() {

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
        val user = randomUser()
        asyncEntityStore.insert(user).thenAccept { u ->
            assertThat(u.id).isNotNull()

            val loaded = asyncEntityStore.select(BasicUser::class).where(BasicUser.ID eq user.id).get().first()
            assertThat(loaded).isEqualTo(user)
        }.join()
    }

    @Test
    fun `async insert and count`() {

        with(asyncEntityStore) {
            val user = randomUser()

            insert(user)
                .thenAccept { savedUser ->
                    assertThat(savedUser.id).isNotNull()
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
            val user = randomUser()

            insert(user)
                .thenApply { savedUser ->
                    val group = BasicGroup().apply { name = "group" }
                    savedUser.groups.add(group)
                    group
                }
                .thenCompose { group ->
                    insert(group)
                }
                .get()

            assertThat(user.groups).hasSize(1)
        }
    }

    @Test
    fun `async query update`() {
        val user = randomUser().apply { age = 100 }

        with(asyncEntityStore) {
            insert(user)
                .thenCompose { savedUser ->
                    update<BasicUser>(BasicUser::class)
                        .set(BasicUser.ABOUT, "nothing")
                        .set(BasicUser.AGE, 50)
                        .where(BasicUser.AGE eq user.age)
                        .get()
                        .toCompletableFuture(Executors.newSingleThreadExecutor())
                }.thenApplyAsync { rowCount ->
                    assertThat(rowCount).isEqualTo(1)
                }
                .get()
        }
    }

    @Test
    fun `async to blocking`() {
        val user = randomUser()
        asyncEntityStore.toBlocking().insert(user)
        assertThat(user.id).isNotNull()
    }

    @Test
    fun `query stream`() {
        val users = randomUsers(100)

        with(asyncEntityStore) {
            insert<BasicUser>(users).join()

            val loadedUsers =
                select(BasicUser::class)
                    .orderBy(BasicUser.NAME.asc().nullsLast())
                    .limit(200)
                    .get()
                    .stream()
                    .map { user -> user }

            assertThat(loadedUsers.count()).isEqualTo(100L)
        }
    }
}