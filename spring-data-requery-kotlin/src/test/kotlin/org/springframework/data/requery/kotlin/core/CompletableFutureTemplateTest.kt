package org.springframework.data.requery.kotlin.core

import io.requery.async.KotlinCompletableEntityStore
import io.requery.kotlin.eq
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.springframework.data.requery.kotlin.domain.AbstractDomainTest
import org.springframework.data.requery.kotlin.domain.RandomData
import org.springframework.data.requery.kotlin.domain.basic.*
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

/**
 * org.springframework.data.requery.kotlin.core.CompletableFutureTemplateTest
 * @author debop
 * @since 18. 6. 2
 */
class CompletableFutureTemplateTest: AbstractDomainTest() {

    companion object {
        private val log = KotlinLogging.logger { }
    }

    private val asyncEntityStore: KotlinCompletableEntityStore<Any> by lazy {
        KotlinCompletableEntityStore(dataStore, ForkJoinPool.commonPool())
    }

    @Before
    fun setup() {
        with(operations) {
            deleteAll(BasicLocation::class)
            deleteAll(BasicGroup::class)
            deleteAll(BasicUser::class)
        }
    }

    @Test
    fun `async insert`() {
        val user = RandomData.randomBasicUser()
        asyncEntityStore.insert(user).thenAccept { u ->
            Assertions.assertThat(u.id).isNotNull()

            val loaded = asyncEntityStore.select(BasicUser::class).where(BasicUser::id eq user.id).get().first()
            Assertions.assertThat(loaded).isEqualTo(user)
        }.join()
    }

    @Test
    fun `async insert and count`() {

        with(asyncEntityStore) {
            val user = RandomData.randomBasicUser()

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
            val user = RandomData.randomBasicUser()

            insert(user)
                .thenApply { savedUser ->
                    val group = BasicGroupEntity().apply { name = "group" }
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
                    update<BasicUserEntity>(BasicUserEntity::class)
                        .set(BasicUserEntity.ABOUT, "nothing")
                        .set(BasicUserEntity.AGE, 50)
                        .where(BasicUserEntity.AGE eq user.age)
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
        val users = RandomData.randomBasicUsers(100)

        with(asyncEntityStore) {
            insert<BasicUser>(users).join()

            val loadedUsers =
                select(BasicUser::class)
                    .orderBy(BasicUserEntity.NAME.asc().nullsLast())
                    .limit(200)
                    .get()
                    .stream()
                    .map { user -> user }

            Assertions.assertThat(loadedUsers.count()).isEqualTo(100L)
        }
    }
}