package com.coupang.springframework.data.requery.domain.basic

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomGroup
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUser
import com.coupang.springframework.data.requery.domain.basic.RandomData.randomUsers
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.requery.Persistable
import io.requery.reactivex.ReactiveEntityStore
import io.requery.reactivex.ReactiveSupport
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.reactivestreams.Subscription
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class ReactiveTest: AbstractDomainTest() {

    companion object: KLogging()

    private val reactiveStore: ReactiveEntityStore<Persistable> by lazy {
        ReactiveSupport.toReactiveStore(dataStore)
    }

    @Before
    fun setup() {
        with(requeryTemplate) {
            deleteAll(BasicGroup::class.java)
            deleteAll(BasicLocation::class.java)
            deleteAll(BasicUser::class.java)
        }
    }

    @Test
    fun `reactive insert`() {
        val user = randomUser()
        val latch = CountDownLatch(1)

        reactiveStore.insert(user)
            .subscribe { saved ->
                assertThat(saved.id).isNotNull()
                val loaded = reactiveStore.select(BasicUser::class.java).where(BasicUser.ID eq saved.id).get().first()
                assertThat(loaded).isEqualTo(saved)
                latch.countDown()
            }

        latch.await()
    }

    @Test
    fun `reactive delete`() {
        val user = randomUser()

        with(reactiveStore) {
            insert(user).blockingGet()
            assertThat(user.id).isNotNull()

            delete(user).blockingGet()

            val loaded = select(BasicUser::class.java)
                .where(BasicUser.ID eq user.id)
                .get()
                .firstOrNull()

            assertThat(loaded).isNull()
        }
    }

    @Test
    fun `insert and get count`() {
        val user = randomUser()

        with(reactiveStore) {
            Observable.just(user)
                .concatMap { u ->
                    insert(u).toObservable()
                }

            val saved = insert(user).blockingGet()
            assertThat(saved).isNotNull

            val count = count(BasicUser::class.java).get().single().blockingGet()
            assertThat(count).isEqualTo(1)
        }
    }

    @Test
    fun `insert one to many`() {
        with(reactiveStore) {
            val user = randomUser()
            insert(user)
                .map {
                    BasicGroup().apply {
                        members.add(it)
                    }
                }
                .flatMap { group ->
                    insert(group)
                }
                .blockingGet()

            assertThat(user.groups).hasSize(1)
        }
    }

    @Test
    fun `query empty entity`() {
        val latch = CountDownLatch(1)

        with(reactiveStore) {
            select(BasicUser::class.java).get().observable()
                .observeOn(Schedulers.io())
                .subscribe({ fail("User가 없어야 하는뎅") },
                           { fail("예외도 나서는 안되는뎅") },
                           { latch.countDown() })
        }

        if(!latch.await(1, TimeUnit.SECONDS)) {
            fail("Timeout이 나야 정상임")
        }
    }

    @Test
    fun `query observable`() {

        with(reactiveStore) {
            insert(randomUsers(30)).subscribeOn(Schedulers.io()).blockingGet()

            val rowCount = AtomicInteger()
            select(BasicUser::class.java).limit(50).get()
                .observable()
                .observeOn(Schedulers.computation())
                .subscribe { user ->
                    assertThat(user.id).isNotNull()
                    rowCount.incrementAndGet()
                }

            Thread.sleep(10)
            assertThat(rowCount.get()).isEqualTo(30)
        }
    }

    @Test
    fun `query self observable map`() {
        val count = AtomicInteger(0)

        with(reactiveStore) {
            val disposable = select(BasicUser::class.java).limit(2).get().observableResult()
                .flatMap { user -> user.observable() }
                .subscribe {
                    count.incrementAndGet()
                }

            insert(randomUser()).blockingGet()
            insert(randomUser()).blockingGet()

            assertThat(count.get()).isEqualTo(3)
            disposable.dispose()
        }
    }

    @Test
    fun `self observable delete`() {
        val count = AtomicInteger()

        with(reactiveStore) {
            val disposable = select(BasicUser::class.java).get().observableResult()
                .subscribe {
                    count.incrementAndGet()
                }

            val user = randomUser()

            insert(user).blockingGet()
            delete(user).blockingGet()

            assertThat(count.get()).isEqualTo(3)
            disposable.dispose()
        }
    }

    @Test
    fun `self observable delete query`() {
        val count = AtomicInteger()

        with(reactiveStore) {
            val disposable = select(BasicUser::class.java).get().observableResult()
                .subscribe {
                    count.incrementAndGet()
                }

            val user = randomUser()

            insert(user).blockingGet()
            assertThat(count.get()).isEqualTo(2)

            val rows = delete(BasicUser::class.java).get().value()
            assertThat(count.get()).isEqualTo(3)
            assertThat(rows).isEqualTo(1)
            disposable.dispose()
        }
    }

    @Test
    fun `query self observable relational`() {
        val count = AtomicInteger()

        with(reactiveStore) {
            val disposable = select(BasicUser::class.java).get().observableResult()
                .subscribe {
                    count.incrementAndGet()
                }

            val user = randomUser()

            insert(user).blockingGet()
            assertThat(count.get()).isEqualTo(2)

            val group = randomGroup()
            user.groups.add(group)
            user.about = "new about"
            update(user).blockingGet()
            assertThat(count.get()).isEqualTo(3)

            delete(user).blockingGet()
            assertThat(count.get()).isEqualTo(4)

            disposable.dispose()
        }
    }

    @Test
    fun `query observable from entity`() {
        with(reactiveStore) {
            val user = randomUser()

            insert(user)
                .map {
                    BasicGroup().apply {
                        members.add(it)
                    }
                }.flatMap { group ->
                    insert(group)
                }
                .blockingGet()

            assertThat(user.groups).hasSize(1)
        }
    }

    @Test
    fun `run in transaction`() {
        with(reactiveStore) {
            val user = randomUser()

            runInTransaction { blocking ->
                blocking.insert(user)
                user.about = "new about"
                blocking.update(user)
                blocking.delete(user)
                Unit
            }.blockingGet()

            assertThat(count(BasicUser::class.java).get().value()).isEqualTo(0)

            val user2 = randomUser()

            runInTransaction { blocking ->
                blocking.insert(user2)
                Unit
            }.blockingGet()

            assertThat(count(BasicUser::class.java).get().value()).isEqualTo(1)
        }
    }

    @Test
    fun `run in transaction from blocking`() {
        with(reactiveStore) {
            val blocking = this.toBlocking()
            Completable.fromCallable {
                blocking.runInTransaction {
                    val user = randomUser()
                    blocking.insert(user)

                    user.about = "new about"
                    blocking.update(user)
                    null
                }
            }
                .subscribe()

            assertThat(count(BasicUser::class.java).get().value()).isEqualTo(1)
        }
    }

    @Test
    fun `query obervable pull`() {
        with(reactiveStore) {
            insert(randomUsers(36)).blockingGet()

            Thread.sleep(10)

            val loadedUsers = mutableListOf<BasicUser>()
            lateinit var subscription: Subscription

            select(BasicUser::class.java)
                .get()
                .flowable()
                .subscribeOn(Schedulers.trampoline())
                .subscribe(
                    {
                        loadedUsers.add(it)
                        if(loadedUsers.size % 10 == 0 && loadedUsers.size > 1) {
                            subscription.request(10)
                        }
                    },
                    {},
                    {},
                    {
                        subscription = it
                        subscription.request(10)
                    }
                )

            assertThat(loadedUsers.size).isEqualTo(36)
        }
    }
}