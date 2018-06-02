package com.coupang.springframework.data.requery.domain.basic

import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUser
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUsers
import io.requery.Persistable
import io.requery.async.CompletableEntityStore
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

    private val asyncEntityStore: CompletableEntityStore<Persistable> by lazy {
        CompletableEntityStore(requeryTemplate.dataStore, ForkJoinPool.commonPool())
    }

    @Before
    fun setup() {
        with(requeryTemplate) {
            deleteAll(BasicLocation::class.java)
            deleteAll(BasicGroup::class.java)
            deleteAll(BasicUser::class.java)
        }
    }

    @Test
    fun `async insert`() {
        val user = randomUser()
        asyncEntityStore.insert(user).thenAccept { u ->
            assertThat(u.id).isNotNull()

            val loaded = asyncEntityStore.select(BasicUser::class.java).where(BasicUser.ID eq user.id).get().first()
            assertThat(loaded).isEqualTo(user)
        }.join()
    }

    @Test
    fun `async insert and count`() {
        val user = randomUser()
        asyncEntityStore.insert(user)
            .thenAccept { user ->
                assertThat(user.id).isNotNull()
            }
            .thenCompose {
                asyncEntityStore.count(BasicUser::class.java).get().toCompletableFuture()
            }
            .get()
    }

    @Test
    fun `async insert one to many`() {
        val user = randomUser()
        asyncEntityStore.insert(user)
            .thenApply { user ->
                val group = BasicGroup().apply { name = "group" }
                user.groups.add(group)
                group
            }
            .thenCompose { group ->
                asyncEntityStore.insert(group)
            }
            .get()

        assertThat(user.groups).hasSize(1)
    }

    @Test
    fun `async query update`() {
        val user = randomUser().apply { age = 100 }

        asyncEntityStore.insert(user)
            .thenCompose { user ->
                asyncEntityStore.update(BasicUser::class.java)
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

    @Test
    fun `async to blocking`() {
        val user = randomUser()
        asyncEntityStore.toBlocking().insert(user)
        assertThat(user.id).isNotNull()
    }

    @Test
    fun `query stream`() {
        val users = randomUsers(100)
        asyncEntityStore.insert(users).join()

        val loadedUsers = asyncEntityStore
            .select(BasicUser::class.java)
            .orderBy(BasicUser.NAME.asc().nullsLast())
            .limit(200)
            .get()
            .stream()
            .map { user -> user }

        assertThat(loadedUsers).hasSize(100)

    }
}