package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.core.ForkJoinExecutor
import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.core.EntityState
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.functional.RandomData.randomPerson
import io.requery.proxy.CompositeKey
import io.requery.proxy.PropertyState
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * FunctionalTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
class FunctionalTest: AbstractDomainTest() {

    companion object: KLogging() {
        const val COUNT = 100
    }

    @Before
    fun setup() {
        requeryKtTmpl.deleteAll(FuncAddress::class)
        requeryKtTmpl.deleteAll(FuncGroup::class)
        requeryKtTmpl.deleteAll(FuncPhone::class)
        requeryKtTmpl.deleteAll(FuncPerson::class)
    }

    @Test
    fun `equals and hashCode`() {
        val p1 = FuncPerson().apply {
            age = 10
            name = "Bob"
            email = "bob@example.com"
        }
        val p2 = FuncPerson().apply {
            age = 10
            name = "Bob"
            email = "bob@example.com"
        }

        assertThat(p1).isEqualTo(p2)
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode())
    }

    @Test
    fun `copyable entity`() {
        val addr = FuncAddress().apply {
            city = "Sanfrancisco"
            state = "CA"
            countryCode = "US"
            zip = "12345"
        }

        val copied = addr.copy()
        assertThat(copied).isEqualTo(addr)
    }

    @Test
    fun `custom converter`() {
        val phone = RandomData.randomPhone()

        requeryTmpl.insert(phone)

        val loaded = requeryTmpl.dataStore
            .select(FuncPhone::class.java)
            .where(FuncPhone.EXTENSIONS.eq(phone.extensions))
            .get()
            .first()

        assertThat(loaded).isEqualTo(phone)
    }

    @Test
    fun `select by id`() {
        val person = randomPerson()

        requeryTmpl.insert(person)
        assertThat(person.id).isGreaterThan(0)

        val loaded = requeryTmpl.findById(FuncPerson::class.java, person.id)!!
        assertThat(loaded).isEqualTo(person)

        val loaded2 = requeryTmpl.dataStore
            .select(FuncPerson::class.java)
            .where(FuncPerson.ID eq person.id)
            .get()
            .firstOrNull()!!

        assertThat(loaded2).isEqualTo(loaded)
    }

    @Test
    fun `insert with default value`() {
        val person = randomPerson()

        requeryTmpl.insert(person)
        assertThat(person.id).isGreaterThan(0)

        val loaded = requeryTmpl.findById(FuncPerson::class.java, person.id)!!
        assertThat(loaded).isEqualTo(person)
        assertThat(loaded.description).isEqualTo("empty")
    }

    @Test
    fun `insert select null key reference`() {
        requeryTmpl.deleteAll(FuncPerson::class.java)

        val person = randomPerson()

        requeryTmpl.insert(person)
        assertThat(person.id).isGreaterThan(0)
        assertThat(person.address).isNull()

        val address = requeryTmpl.dataStore.select(FuncPerson::class.java).get().first().address
        assertThat(address).isNull()
    }

    @Test
    fun `insert many people with transaction`() {
        requeryTmpl.runInTransaction {

            for(i in 0 until COUNT) {
                val person = randomPerson()
                insert(person)
            }

            val personCount = requeryTmpl.count(FuncPerson::class.java)
            assertThat(personCount).isEqualTo(COUNT.toLong())
        }
    }

    @Test
    fun `insert many people with batch`() {
        val people = List(COUNT) { randomPerson() }

        // Batch 방식으로 저장한다.
        requeryTmpl.insertAll(people)

        val personCount = requeryTmpl.count(FuncPerson::class.java)
        assertThat(personCount).isEqualTo(COUNT.toLong())
    }

    @Test
    fun `insert many people with multi thread`() {
        // HINT: Executor를 이용하여 비동기 방식으로 데이터 작업하기
        // HINT: Coroutine 을 이용하는 것이 더 낫다 (Transaction 도 되고)

        val executor = ForkJoinExecutor

        try {
            val latch = CountDownLatch(COUNT)
            val map = ConcurrentHashMap<Long, FuncPerson>()

            for(i in 0 until COUNT) {
                executor.submit {
                    val person = randomPerson()
                    requeryTmpl.insert(person)
                    assertThat(person.id).isGreaterThan(0)
                    map[person.id!!] = person
                    latch.countDown()
                }
            }
            assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue()

            map.forEach { id, person ->
                val loaded = requeryTmpl.findById(FuncPerson::class.java, id)!!
                assertThat(loaded).isEqualTo(person)
            }
        } finally {
            executor.shutdown()
        }
    }

    @Test
    fun `insert many people with coroutines`() {
        val map = ConcurrentHashMap<Long, FuncPerson>()
        runBlocking {
            val jobs = List(COUNT) {
                async(Unconfined) {
                    val person = randomPerson()
                    requeryKtTmpl.insert(person)
                    assertThat(person.id).isGreaterThan(0)
                    map[person.id!!] = person
                }
            }
            jobs.forEach { it.join() }

            val loadJobs = map.map { (id, person) ->
                launch(Unconfined) {
                    val loaded = requeryKtTmpl.findById(FuncPerson::class, id)!!
                    assertThat(loaded).isEqualTo(person)
                }
            }
            loadJobs.forEach { it.join() }
        }
    }

    @Test
    fun `insert empty entity`() {
        val phone = FuncPhone()
        requeryTmpl.insert(phone)
        assertThat(phone.id).isNotNull()
    }

    class DerivedPhone: FuncPhone() {
        override fun toString(): String = "DerivedPhone"
    }

    @Test
    fun `insert derived class`() {
        val derivedPhone = DerivedPhone().apply {
            phoneNumber = "555-5555"
        }
        requeryTmpl.insert(derivedPhone)
        assertThat(derivedPhone.id).isNotNull()

        val loaded = requeryTmpl.findById(FuncPhone::class.java, derivedPhone.id)
        assertThat(loaded).isNotNull

        // NOTE: 하지만 FuncPhone 을 DerivedPhone으로 casting 은 하지 못한다 !!!
    }

    @Test
    fun `insert by query`() {
        requeryKtTmpl.withDataStore {
            val id1 = insert(FuncPerson::class)
                .value(FuncPerson.ABOUT, "nothing")
                .value(FuncPerson.AGE, 50)
                .get()
                .first()
                .get<Long?>(FuncPerson.ID)

            assertThat(id1).isNotNull().isGreaterThan(0)

            val id2 = insert(FuncPerson::class)
                .value(FuncPerson.NAME, "Bob")
                .value(FuncPerson.AGE, 50)
                .get()
                .first()
                .get<Long?>(FuncPerson.ID)

            assertThat(id2).isNotNull().isGreaterThan(0).isNotEqualTo(id1)
        }
    }

    @Test
    fun `insert into select query`() {
        // NOTE: Java 와 Kotlin 으로 만든 Entity가 다르다. Kotlin 으로 만든 Entity에 대해서는 KotlinEntityDataStore를 사용해야 한다.
        //
        requeryKtTmpl.withDataStore {
            val group = FuncGroup().apply {
                name = "Bob"
                description = "Bob's group"
            }

            insert(group)

            // NOTE: Java 와 Kotlin 으로 만든 Entity가 다르다. Kotlin 으로 만든 Entity에 대해서는 KotlinEntityDataStore를 사용해야 한다.
            //
            val count = insert(FuncPerson::class, FuncPerson.NAME, FuncPerson.DESCRIPTION)
                .query(select(FuncGroup.NAME, FuncGroup.DESCRIPTION))
                .get()
                .first()
                .count()

            assertThat(count).isEqualTo(1)

            val person = select(FuncPerson::class).get().first()
            assertThat(person.name).isEqualTo(group.name)
            assertThat(person.description).isEqualTo(group.description)
        }
    }

    @Test
    fun `insert with transaction callable`() {
        val result = requeryKtTmpl.withTransaction {
            List(COUNT) {
                val person = randomPerson()
                insert(person)
                assertThat(person.id).isNotNull()
            }
            "success"
        }
        assertThat(result).isEqualTo("success")
    }

    @Test
    fun `find by composite key`() {
        requeryKtTmpl.withDataStore {
            val group = FuncGroup().apply {
                name = "grou"
                type = GroupType.PRIVATE
            }
            val person = randomPerson()
            person.groups.add(group)

            insert(person)
            assertThat(person.id).isNotNull()

            val map = mutableMapOf(Func_Group_Members.FUNC_GROUP_ID to group.id,
                                   Func_Group_Members.FUNC_PERSON_ID to person.id)

            val compositeKey = CompositeKey(map)

            val joined = findByKey(Func_Group_Members::class, compositeKey)!!
            assertThat(joined.funcPersonId).isEqualTo(person.id)
            assertThat(joined.funcGroupId).isEqualTo(group.id)
        }
    }

    @Test
    fun `find by key and delete`() {
        requeryKtTmpl.withDataStore {
            val person = randomPerson()
            val address = RandomData.randomAddress()

            person.address = address

            insert(person)
            assertThat(person.id).isNotNull()

            var loaded = findByKey(FuncPerson::class, person.id)
            assertThat(loaded).isNotNull
            assertThat(loaded?.address).isEqualTo(address)

            delete(loaded!!)

            loaded = findByKey(FuncPerson::class, person.id)
            assertThat(loaded).isNull()
        }

        assertThat(requeryKtTmpl.count(FuncAddress::class)).isEqualTo(0)
    }

    @Test
    fun `find by key and delete inverse`() {
        requeryKtTmpl.withDataStore {
            val person = randomPerson()
            val address = RandomData.randomAddress()

            person.address = address

            insert(person)
            assertThat(person.id).isNotNull()

            delete(person)

            val loaded = findByKey(FuncPerson::class, person.id)
            assertThat(loaded).isNull()
        }
        assertThat(requeryKtTmpl.count(FuncAddress::class)).isEqualTo(0)
    }

    @Test
    fun `rollback transaction`() {
        val ids = mutableListOf<Long>()

        try {
            requeryKtTmpl.withTransaction {
                List(COUNT) {
                    val person = randomPerson()
                    insert(person)
                    assertThat(person.id).isNotNull()
                    ids.add(person.id!!)

                    if(it == 5)
                        throw RuntimeException("rollback ...")
                }
            }
        } catch(ignored: Exception) {
            log.info { "Rollback executed." }
        }

        assertThat(requeryKtTmpl.count(FuncPerson::class)).isEqualTo(0)
    }

    @Test
    fun `check changed attribute`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)
        assertThat(person.id).isNotNull()

        person.name = "Bob Smith"
        person.birthday = LocalDate.of(1983, 11, 14)

        // HINT: Proxy를 통해 entity의 변화관리를 조회할 수 있다
        val proxy = FuncPerson.`$TYPE`.proxyProvider.apply(person)
        var count = 0
        FuncPerson.`$TYPE`.attributes.forEach { attr ->
            if(proxy.getState(attr) == PropertyState.MODIFIED) {
                log.debug { "modified attr=${attr.name}" }
                count++
            }
        }
        assertThat(count).isEqualTo(2)
        requeryKtTmpl.update(person)

        FuncPerson.`$TYPE`.attributes.forEach { attr ->
            if(proxy.getState(attr) == PropertyState.MODIFIED) {
                fail("변경된 것이 없어야 합니다.")
            }
        }
    }

    @Test
    fun `update no changed entity`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)
        assertThat(person.id).isNotNull()

        requeryKtTmpl.update(person)
    }

    @Test
    fun `entity listener`() {
        val person = randomPerson()

        requeryKtTmpl.insert(person)
        assertThat(person.previousState).isEqualTo(EntityState.PRE_SAVE)
        assertThat(person.currentState).isEqualTo(EntityState.POST_SAVE)

        person.email = "bob@example.com"
        requeryKtTmpl.update(person)
        assertThat(person.previousState).isEqualTo(EntityState.PRE_UPDATE)
        assertThat(person.currentState).isEqualTo(EntityState.POST_UPDATE)

        requeryKtTmpl.delete(person)
        assertThat(person.previousState).isEqualTo(EntityState.PRE_DELETE)
        assertThat(person.currentState).isEqualTo(EntityState.POST_DELETE)
    }

    @Test
    fun `insert one to one entity with null association`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)
        assertThat(person.address).isNull()
    }

    @Test
    fun `insert one to one entity with null association inverse`() {
        val address = RandomData.randomAddress()
        requeryKtTmpl.insert(address)
        assertThat(address.person).isNull()
    }

    @Test
    fun `insert one to one`() {
        val address = RandomData.randomAddress()
        requeryKtTmpl.insert(address)
        assertThat(address.id).isNotNull()

        val person = randomPerson()
        requeryKtTmpl.insert(person)
        assertThat(person.id).isNotNull()

        person.address = address
        requeryKtTmpl.update(person)

        // fetch inverse
        assertThat(address.person).isEqualTo(person)

        // unset
        person.address = null
        requeryKtTmpl.update(person)

        // HINT: address는 update하지 않았지만 association에 변화를 적용하려면 refresh를 수행해야 한다
        requeryKtTmpl.refresh(address)
        assertThat(address.person).isNull()


        val loaded = requeryKtTmpl.findById(FuncPerson::class, person.id)!!
        assertThat(loaded.address).isNull()
    }

    @Test
    fun `insert one to one with cascade`() {
        val address = RandomData.randomAddress()
        val person = randomPerson()
        person.address = address

        requeryKtTmpl.insert(person)

        // HINT: JPA는 양방향에 대해 모두 설정해주어야 하지만, requery는 insert 시에 cascade 해준다
        assertThat(address.person).isEqualTo(person)

        val loadedAddr = requeryKtTmpl.findById(FuncAddress::class, address.id)!!
        assertThat(loadedAddr.person).isEqualTo(person)
    }

    @Test
    fun `refresh all`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone = RandomData.randomPhone()
        person.phoneNumbers.add(phone)

        requeryKtTmpl.update(person)
        requeryKtTmpl.refreshAll(person)

        assertThat(person.phoneNumberList).contains(phone)
    }

    @Test
    fun `refresh multiple`() {
        val people = List(10) { randomPerson() }
        requeryKtTmpl.insertAll(people)

        val count = requeryKtTmpl.dataStore.update(FuncPerson::class).set(FuncPerson.NAME, "fff").get().value()
        assertThat(count).isEqualTo(people.size)

        // refreshAlL 을 각각의 entity에 대해서 호출해줘야 제대로 반영된다.
        requeryKtTmpl.refreshAll(people)
        requeryKtTmpl.refresh(people, FuncPerson.NAME)

        people.forEach {
            log.debug { "person name=${it.name}" }
        }
        assertThat(people.all { it.name == "fff" }).isTrue()
    }

    @Test
    fun `refresh attributes`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone = RandomData.randomPhone()
        person.phoneNumbers.add(phone)

        requeryKtTmpl.update(person)
        requeryKtTmpl.refresh(person, FuncPerson.NAME, FuncPerson.PHONE_NUMBER_LIST, FuncPerson.ADDRESS, FuncPerson.EMAIL)

        assertThat(person.phoneNumberSet).contains(phone)
    }

    @Test
    fun `version increment`() {
        val group = FuncGroup().apply {
            name = "group"
            type = GroupType.PRIVATE
        }
        requeryKtTmpl.insert(group)

        group.type = GroupType.PUBLIC
        requeryKtTmpl.update(group)
        requeryKtTmpl.refresh(group, FuncGroup.VERSION)

        log.debug { "Group version=${group.version}" }
        assertThat(group.version).isGreaterThan(0)

        val group2 = FuncGroup().apply {
            name = "group2"
            type = GroupType.PRIVATE
        }

        requeryKtTmpl.insert(group2)
        requeryKtTmpl.refresh(listOf(group, group2), FuncGroup.VERSION)

        log.debug { "Group version=${group.version}" }
        log.debug { "Group2 version=${group2.version}" }
    }

    @Test
    fun `version udpate`() {
        val group = FuncGroup().apply { name = "Test1" }

        requeryKtTmpl.upsert(group)
        assertThat(group.version).isGreaterThan(0)

        group.name = "Test2"
        requeryKtTmpl.upsert(group)
        assertThat(group.version).isGreaterThan(1)

        group.name = "Test3"
        requeryKtTmpl.upsert(group)
        assertThat(group.version).isGreaterThan(2)
    }

    @Test
    fun `fill result by collect`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val people = requeryKtTmpl.dataStore.select(FuncPerson::class).get().collect(hashSetOf<FuncPerson>())
        assertThat(people).hasSize(1)
    }

    @Test
    fun `result to list`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val people = requeryKtTmpl.dataStore.select(FuncPerson::class).get().toList()
        assertThat(people).hasSize(1)
    }
}