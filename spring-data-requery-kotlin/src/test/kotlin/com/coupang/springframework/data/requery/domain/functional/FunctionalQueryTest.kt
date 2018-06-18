package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.functional.RandomData.randomAddress
import com.coupang.springframework.data.requery.domain.functional.RandomData.randomPerson
import com.coupang.springframework.data.requery.domain.functional.RandomData.randomPersons
import com.coupang.springframework.data.requery.domain.functional.RandomData.randomPhone
import io.requery.PersistenceException
import io.requery.query.NamedNumericExpression
import io.requery.query.Result
import io.requery.query.function.Case
import io.requery.query.function.Coalesce
import io.requery.query.function.Count
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * FunctionalQueryTest
 *
 * @author debop@coupang.com
 * @since 18. 6. 1
 */
class FunctionalQueryTest: AbstractDomainTest() {

    companion object: KLogging() {
        const val COUNT = 100
    }

    @Before
    fun setup() {
        with(requeryKotlin) {
            deleteAll(FuncAddress::class)
            deleteAll(FuncGroup::class)
            deleteAll(FuncPhone::class)
            deleteAll(FuncPerson::class)
        }
    }

    @Test
    fun `query function now`() {
        with(requeryKotlin) {
            val person = randomPerson()
            person.birthday = LocalDate.now().plusDays(1)
            insert(person)

            val result = select(FuncPerson::class)
                .where(FuncPerson.BIRTHDAY.gt(LocalDate.now()))
                .get()

            assertThat(result.toList()).hasSize(1)
        }
    }

    @Test
    fun `query function random`() {
        with(requeryKotlin) {
            insertAll(randomPersons(10))

            val result = select(FuncPerson::class)
                .orderBy(io.requery.query.function.Random())
                .get()

            assertThat(result.toList()).hasSize(10)
        }
    }

    @Test fun `single query where`() {
        val name = "duplicateFirstname"

        List(10) {
            requeryKotlin.insert(randomPerson().also { it.name = name })
        }

        val result = requeryKotlin.select(FuncPerson::class)
            .where(FuncPerson.NAME.eq(name))
            .get()

        assertThat(result.toList()).hasSize(10)
    }

    @Test fun `single query where not`() {
        val name = "firstName"
        val email = "not@test.io"

        List(10) {
            val person = randomPerson()
            when(it) {
                0 -> person.name = name
                1 -> person.email = email
            }
            requeryKotlin.insert(person)
        }

        // FIXME: not 연산자가 제대로 동작하지 않는다. (Java 와 Kotlin 모두)
        //
        val result = requeryKotlin.select(FuncPerson::class)
            .where(
                FuncPerson.NAME.ne(name).and(FuncPerson.EMAIL.ne(email))
            )
            .get()

        assertThat(result.toList()).hasSize(8)
    }

    @Test
    fun `single query execute`() {
        with(requeryKotlin) {
            insertAll(RandomData.randomPersons(10))

            val result: Result<FuncPerson> = select(FuncPerson::class).get()
            assertThat(result.toList()).hasSize(10)

            val person = randomPerson()
            insert(person)

            // 이렇게 Result<T> 로 얻은 객체는 조건을 가진 Query 구문이라고 봐도 될 듯
            assertThat(result.toList()).hasSize(11)
        }
    }

    @Test
    fun `single query with limit and offset`() {
        with(requeryKotlin) {
            val name = "duplicateFirstName"
            List(10) {
                insert(randomPerson().also { it.name = name })
            }

            for(i in 0 until 3) {
                val query = select(FuncPerson::class)
                    .where(FuncPerson.NAME.eq(name))
                    .orderBy(FuncPerson.NAME)
                    .limit(5)
                    .get()
                assertThat(query.toList()).hasSize(5)

                val query2 = select(FuncPerson::class)
                    .where(FuncPerson.NAME.eq(name))
                    .limit(5)
                    .offset(5)
                    .get()

                assertThat(query2.toList()).hasSize(5)
            }
        }
    }

    @Test
    fun `single query where null`() {
        with(requeryKotlin) {
            val person = randomPerson()
            person.name = null
            insert(person)

            val query = select(FuncPerson::class)
                .where(FuncPerson.NAME.isNull())
                .get()

            assertThat(query.toList()).hasSize(1)
        }
    }


    @Test
    fun `delete all`() {
        with(requeryKotlin) {
            val name = "someName"
            List(10) {
                val person = randomPerson()
                person.name = name
                insert(person)
            }
            assertThat(deleteAll(FuncPerson::class)).isGreaterThan(0)
            assertThat(select(FuncPerson::class).get().firstOrNull()).isNull()
        }
    }

    @Test
    fun `delete batch`() {
        with(requeryKotlin) {
            val people = randomPersons(COUNT)
            insertAll(people)
            // refresh(people)

            assertThat(count(FuncPerson::class).get().value()).isEqualTo(people.size)

            requeryKotlin.deleteAll(people)
            assertThat(count(FuncPerson::class).get().value()).isEqualTo(0)
        }
    }

    @Test
    fun `query by foreign key`() {
        with(requeryKotlin) {
            val person = randomPerson()
            insert(person)

            val phone1 = randomPhone()
            val phone2 = randomPhone()
            person.phoneNumbers.add(phone1)
            person.phoneNumbers.add(phone2)

            upsert(person)
            assertThat(person.phoneNumberSet).containsOnly(phone1, phone2)

            // by entity
            val query1 = select(FuncPhone::class).where(FuncPhone.OWNER eq person).get()

            assertThat(query1.toList()).hasSize(2).containsOnly(phone1, phone2)
            assertThat(person.phoneNumberList).hasSize(2).containsAll(query1.toList())

            // by id
            val query2 = select(FuncPhone::class).where(FuncPhone.OWNER_ID.eq(person.id)).get()

            assertThat(query2.toList()).hasSize(2).containsOnly(phone1, phone2)
            assertThat(person.phoneNumberList).hasSize(2).containsAll(query2.toList())
        }
    }

    @Test
    fun `query by UUID`() {
        with(requeryKotlin) {
            val person = randomPerson()
            insert(person)

            val uuid = person.uuid
            val loaded = select(FuncPerson::class).where(FuncPerson.UUID.eq(uuid)).get().first()
            assertThat(loaded).isEqualTo(person)
        }
    }

    @Test
    fun `query select distinct`() {
        with(requeryKotlin) {
            repeat(10) {
                val person = randomPerson().apply { name = (it / 2).toString() }
                insert(person)
            }

            val result = select(FuncPerson.NAME).distinct().get()

            assertThat(result.toList()).hasSize(5)
        }
    }

    @Test
    fun `query select count`() {
        with(requeryKotlin) {
            val people = randomPersons(10)
            insertAll(people)

            // HINT: count 가져오기 : sql 함수를 사용하는구나 !!!
            //
            // select count(*) as bb from FuncPerson

            select(Count.count(FuncPerson::class.java).`as`("bb"))
                .get()
                .use { result ->
                    assertThat(result.first().get<Int>("bb")).isEqualTo(people.size)
                }

            select(Count.count(FuncPerson::class.java))
                .get()
                .use { result ->
                    assertThat(result.first().get<Int>(0)).isEqualTo(people.size)
                }

            assertThat(count(FuncPerson::class).get().value()).isEqualTo(people.size)

            count(FuncPerson::class).get().consume { count ->
                assertThat(count).isEqualTo(10)
            }
        }
    }

    @Test
    fun `query select count where`() {
        with(requeryKotlin) {
            insert(randomPerson().apply { name = "countme" })
            insertAll(randomPersons(9))

            assertThat(count(FuncPerson::class).where(FuncPerson.NAME.eq("countme")).get().value().toInt()).isEqualTo(1)

            select(Count.count(FuncPerson::class.java).`as`("cnt"))
                .where(FuncPerson.NAME.eq("countme"))
                .get()
                .use { result ->
                    assertThat(result.first().get<Int>("cnt")).isEqualTo(1)
                }
        }
    }

    @Test
    fun `query not null`() {
        with(requeryKotlin) {
            insertAll(randomPersons(10))

            val result = select(FuncPerson::class).where(FuncPerson.NAME.notNull()).get()
            assertThat(result.toList()).hasSize(10)
        }
    }

    @Test
    fun `query from sub query`() {
        //
        // BUG: 현재 KotlinTemlate에서는 제대로 안된다. Java 버전의 RequeryTemplate에서는 제대로 된다.
        //
        with(requeryTemplate) {
            repeat(10) {
                val person = randomPerson().apply { age = it + 1 }
                insert(person)
            }

            val personAge = FuncPerson.AGE

            val subQuery = select(personAge.sum().`as`("avg_age"))
                .from(FuncPerson::class.java)
                .groupBy(personAge)
                .`as`("sums")

            //            subQuery.get().forEach {
            //                log.debug { "row=$it" }
            //            }

            val result =
                select(NamedNumericExpression.ofInteger("avg_age").avg())
                    .from(subQuery)
                    .get()

            assertThat(result.first().get<Int>(0)).isGreaterThanOrEqualTo(5)
        }
    }

    @Test
    fun `query join orderBy`() {
        with(requeryKotlin) {
            val person = randomPerson()
            person.address = randomAddress()
            insert(person)

            // not a useful query just tests the sql output
            val result = select(FuncAddress::class)
                .join(FuncPerson::class).on(FuncPerson.ADDRESS_ID.eq(FuncAddress.ID))
                .where(FuncPerson.ID.eq(person.id))
                .orderBy(FuncAddress.CITY.desc())
                .get()

            val addresses = result.toList()
            assertThat(addresses.size).isGreaterThan(0)
        }
    }

    @Test
    fun `query select min`() {
        with(requeryKotlin) {
            repeat(9) { insert(randomPerson()) }
            insert(randomPerson().apply { birthday = LocalDate.of(1800, 11, 11) })

            val query = select(FuncPerson.BIRTHDAY.min().`as`("oldestBday")).get()
            query.use {
                val birthday: LocalDate = it.first().get("oldestBday")
                assertThat(birthday).isEqualTo(LocalDate.of(1800, 11, 11))
            }
        }
    }

    @Test
    fun `query select trim`() {
        with(requeryKotlin) {
            val person = randomPerson().apply { name = "  Name  " }
            insert(person)

            val result = select(FuncPerson.NAME.trim().`as`("name")).get().first()
            val name = result.get<String>(0)
            assertThat(name).isEqualTo("Name")
        }
    }

    @Test
    fun `query select substr`() {
        with(requeryKotlin) {
            val person = randomPerson().apply { name = "  Name" }
            insert(person)

            val result = select(FuncPerson.NAME.substr(3, 6).`as`("name")).get().first()
            val name = result.get<String>(0)
            assertThat(name).isEqualTo("Name")
        }
    }

    @Test
    fun `query orderBy`() {
        with(requeryKotlin) {
            repeat(10) {
                val person = randomPerson().apply { age = it }
                insert(person)
            }

            val query = select(FuncPerson.AGE).orderBy(FuncPerson.AGE.desc()).get()

            val topAge = query.first().get<Int>(0)
            assertThat(topAge).isEqualTo(9)
        }
    }

    @Test
    fun `query orderBy function`() {
        with(requeryKotlin) {
            insert(randomPerson().apply { name = "BOBB" })
            insert(randomPerson().apply { name = "BobA" })
            insert(randomPerson().apply { name = "bobC" })

            val people = select(FuncPerson.NAME)
                .orderBy(FuncPerson.NAME.upper().desc())
                .get()
                .toList()

            assertThat(people).hasSize(3)
            assertThat(people[0].get<String>(0)).isEqualTo("bobC")
            assertThat(people[1].get<String>(0)).isEqualTo("BOBB")
            assertThat(people[2].get<String>(0)).isEqualTo("BobA")
        }
    }

    @Test
    fun `query groupBy`() {
        with(requeryKotlin) {
            repeat(5) {
                insert(randomPerson().apply { age = it })
            }

            val result = select(FuncPerson.AGE)
                .groupBy(FuncPerson.AGE)
                .having(FuncPerson.AGE.gt(3))
                .get()

            assertThat(result.toList()).hasSize(1)

            val result2 = select(FuncPerson.AGE)
                .groupBy(FuncPerson.AGE)
                .having(FuncPerson.AGE.lt(0))
                .get()

            assertThat(result2.toList()).isEmpty()
        }
    }

    @Test
    fun `query select where in`() {
        with(requeryKotlin) {
            val name = "Hello!"
            val person = randomPerson().also { it.name = name }
            insert(person)

            val group = FuncGroup().also { it.name = name }
            insert(group)

            person.groups.add(group)
            update(person)

            val groupNames = select(FuncGroup.NAME)
                .where(FuncGroup.NAME.eq(name))

            var p = select(FuncPerson::class)
                .where(FuncPerson.NAME.`in`(groupNames)).get().first()
            assertThat(p.name).isEqualTo(name)

            p = select(FuncPerson::class)
                .where(FuncPerson.NAME.notIn(groupNames)).get().firstOrNull()
            assertThat(p).isNull()

            p = select(FuncPerson::class)
                .where(FuncPerson.NAME.`in`(listOf("Hello!", "Other"))).get().first()
            assertThat(p.name).isEqualTo(name)

            p = select(FuncPerson::class)
                .where(FuncPerson.NAME.`in`(listOf("Hello!"))).get().first()
            assertThat(p.name).isEqualTo(name)

            p = select(FuncPerson::class)
                .where(FuncPerson.NAME.notIn(listOf("Hello!"))).get().firstOrNull()
            assertThat(p).isNull()
        }
    }

    @Test
    fun `query between`() {
        with(requeryKotlin) {
            val person = randomPerson()
            person.age = 75
            insert(person)

            val p = select(FuncPerson::class).where(FuncPerson.AGE.between(50, 100)).get().first()
            assertThat(p).isEqualTo(person)
        }
    }

    @Test
    fun `query conditions`() {
        with(requeryKotlin) {
            val person = randomPerson()
            person.age = 75
            insert(person)

            var p = select(FuncPerson::class).where(FuncPerson.AGE.gte(75)).get().first()
            assertThat(p).isEqualTo(person)

            p = select(FuncPerson::class).where(FuncPerson.AGE.lte(75)).get().first()
            assertThat(p).isEqualTo(person)

            p = select(FuncPerson::class).where(FuncPerson.AGE.gt(75)).get().firstOrNull()
            assertThat(p).isNull()

            p = select(FuncPerson::class).where(FuncPerson.AGE.lt(75)).get().firstOrNull()
            assertThat(p).isNull()

            p = select(FuncPerson::class).where(FuncPerson.AGE.ne(75)).get().firstOrNull()
            assertThat(p).isNull()

        }
    }

    @Test
    fun `query compound conditions`() {
        with(requeryKotlin) {
            val person1 = randomPerson().apply { age = 75 }
            insert(person1)

            val person2 = randomPerson().apply { age = 10; name = "Carol" }
            insert(person2)

            val person3 = randomPerson().apply { age = 0; name = "Bob" }
            insert(person3)

            var result = select(FuncPerson::class)
                .where(FuncPerson.AGE.gt(5).and(FuncPerson.AGE.lt(75)).and(FuncPerson.NAME.ne("Bob")))
                .or(FuncPerson.NAME.eq("Bob"))
                .get()

            assertThat(result.toList()).hasSize(2).containsOnly(person2, person3)

            result = select(FuncPerson::class)
                .where(FuncPerson.AGE.gt(5).or(FuncPerson.AGE.lt(75)))
                .and(FuncPerson.NAME.eq("Bob"))
                .get()

            assertThat(result.toList()).hasSize(1).containsOnly(person3)
        }
    }

    @Test
    fun `query consume`() {
        with(requeryKotlin) {
            insertAll(randomPersons(10))

            var count = 0
            val result = select(FuncPerson::class).get()
            result.each {
                count++
            }
            assertThat(count).isEqualTo(10)
        }
    }

    @Test
    fun `query map`() {
        with(requeryKotlin) {
            insert(randomPerson().apply { email = "one@test.com" })
            insertAll(randomPersons(9))

            val result = select(FuncPerson::class).get()
            var map = result.toMap(FuncPerson.EMAIL, ConcurrentHashMap<String, FuncPerson>())
            assertThat(map["one@test.com"]).isNotNull

            map = result.toMap(FuncPerson.EMAIL)
            assertThat(map["one@test.com"]).isNotNull

            val kmap = result.toList().map { it.email to it }.toMap()
            assertThat(kmap["one@test.com"]).isNotNull
        }
    }

    @Test
    fun `query update`() {
        with(requeryKotlin) {
            val person = randomPerson().apply { age = 100 }
            insert(person)

            val updatedCount = update(FuncPerson::class)
                .set(FuncPerson.ABOUT, "nothing")
                .set(FuncPerson.AGE, 50)
                .where(FuncPerson.AGE.eq(100))
                .get()
                .value()

            assertThat(updatedCount).isEqualTo(1)
        }
    }

    @Test
    fun `query update refresh`() {
        with(requeryKotlin) {
            val person = randomPerson().apply { age = 100 }
            insert(person)

            val updatedCount = update(FuncPerson::class)
                .set(FuncPerson.AGE, 50)
                .where(FuncPerson.ID.eq(person.id))
                .get()
                .value()

            assertThat(updatedCount).isEqualTo(1)

            val selected = select(FuncPerson::class).where(FuncPerson.ID.eq(person.id)).get().first()
            assertThat(selected.age).isEqualTo(50)
        }
    }

    @Test
    fun `query coalesce`() {
        with(requeryKotlin) {
            var person = randomPerson().apply {
                name = "Carol"
                email = null
            }
            insert(person)

            person = randomPerson().apply {
                name = "Bob"
                email = "test@test.com"
                homepage = null
            }
            insert(person)

            val result = select(Coalesce.coalesce(FuncPerson.EMAIL, FuncPerson.NAME)).get()
            val list = result.toList()
            val values = list.map { it.get<Any?>(0)?.toString() ?: "" }

            assertThat(values).hasSize(2).containsOnly("Carol", "test@test.com")
        }
    }

    @Test
    fun `query like`() {
        with(requeryKotlin) {
            insert(randomPerson().apply { name = "Carol" })
            insert(randomPerson().apply { name = "Bob" })

            var p = select(FuncPerson::class)
                .where(FuncPerson.NAME.like("B%"))
                .get()
                .first()

            assertThat(p.name).isEqualTo("Bob")

            p = select(FuncPerson::class)
                .where(FuncPerson.NAME.lower().like("b%"))
                .get()
                .first()

            assertThat(p.name).isEqualTo("Bob")

            val p2 = select(FuncPerson::class)
                .where(FuncPerson.NAME.notLike("B%"))
                .get()
                .firstOrNull()

            assertThat(p2?.name).isEqualTo("Carol")
        }
    }

    @Test
    fun `query equals ignore case`() {
        with(requeryKotlin) {
            val person = randomPerson().apply { name = "Carol" }
            insert(person)

            val p = select(FuncPerson::class)
                .where(FuncPerson.NAME.equalsIgnoreCase("carol"))
                .get()
                .first()

            assertThat(p).isEqualTo(person)
        }
    }

    @Test
    fun `query case`() {
        with(requeryKotlin) {
            val names = listOf("Carol", "Bob", "Jack")
            names.forEach { name ->
                insert(randomPerson().also { it.name = name })
            }

            var a =
                select(
                    FuncPerson.NAME,
                    Case.type(String::class.java)
                        .`when`(FuncPerson.NAME.eq("Bob"), "B")
                        .`when`(FuncPerson.NAME.eq("Carol"), "C")
                        .elseThen("Unknown")
                )
                    .from(FuncPerson::class)
                    .orderBy(FuncPerson.NAME)
                    .get()

            var list = a.toList()
            assertThat(list[0].get<String>(1)).isEqualTo("B")
            assertThat(list[1].get<String>(1)).isEqualTo("C")
            assertThat(list[2].get<String>(1)).isEqualTo("Unknown")

            a = select(
                FuncPerson.NAME,
                Case.type(Int::class.java)
                    .`when`(FuncPerson.NAME.eq("Bob"), 1)
                    .`when`(FuncPerson.NAME.eq("Carol"), 2)
                    .elseThen(0)
            )
                .orderBy(FuncPerson.NAME)
                .get()

            list = a.toList()
            assertThat(list[0].get<Int>(1)).isEqualTo(1)
            assertThat(list[1].get<Int>(1)).isEqualTo(2)
            assertThat(list[2].get<Int>(1)).isEqualTo(0)
        }
    }

    @Test
    fun `query union`() {
        with(requeryKotlin) {
            val person = randomPerson().apply { name = "Carol" }
            insert(person)

            val group = FuncGroup().apply { name = "Hello!" }
            insert(group)

            // select name as name from FuncPerson
            // union
            // select name as name from FuncGroup order by name
            val result = select(FuncPerson.NAME.`as`("name"))
                .union()
                .select(FuncGroup.NAME.`as`("name"))
                .orderBy(FuncGroup.NAME.`as`("name"))
                .get()
                .toList()

            assertThat(result[0].get<String>(0)).isEqualTo("Carol")
            assertThat(result[1].get<String>(0)).isEqualTo("Hello!")
        }
    }

    @Test
    fun `query raw`() {
        with(requeryKotlin) {
            val count = 5
            val people = List(count) { insert(randomPerson()) }

            val resultIds = mutableListOf<Long>()

            raw("select * from FuncPerson").use { result ->
                val rows = result.toList()
                assertThat(rows).hasSize(count)

                rows.forEachIndexed { index, row ->
                    val name = row.get<String>("name")
                    assertThat(name).isEqualTo(people[index].name)
                    val id = row.get<Long>("personId")
                    assertThat(id).isEqualTo(people[index].id)
                    resultIds.add(id)
                }
            }

            raw("select * from FuncPerson WHERE personId in ?", resultIds).use { result ->
                val rows = result.toList()
                val ids = rows.map { it.get<Long>("personId") }
                assertThat(ids).isEqualTo(resultIds)
            }

            raw("select count(*) from FuncPerson").use { result ->
                val number = result.first().get<Number>(0).toInt()
                assertThat(number).isEqualTo(count)
            }

            raw("select * from FuncPerson WHERE personId = ?", people[0]).use { result ->
                assertThat(result.first().get<Long>("personId")).isEqualTo(people[0].id)
            }
        }
    }

    @Test
    fun `query raw entities`() {
        with(requeryKotlin) {
            val count = 5
            val people = List(count) { insert(randomPerson()) }

            val resultIds = mutableListOf<Long>()

            raw(FuncPerson::class, "select * from FuncPerson").use { result ->
                val rows = result.toList()
                assertThat(rows).hasSize(count)

                rows.forEachIndexed { index, row ->
                    val name = row.name
                    assertThat(name).isEqualTo(people[index].name)
                    val id = row.id
                    assertThat(id).isEqualTo(people[index].id)
                    resultIds.add(id!!)
                }
            }

            raw(FuncPerson::class, "select * from FuncPerson WHERE personId in ?", resultIds).use { result ->
                val rows = result.toList()
                val ids = rows.map { it.id }
                assertThat(ids).isEqualTo(resultIds)
            }

            raw(FuncPerson::class, "select * from FuncPerson WHERE personId = ?", people[0]).use { result ->
                assertThat(result.first().id).isEqualTo(people[0].id)
            }
        }
    }

    @Test
    fun `query union join on same entities`() {
        with(requeryKotlin) {
            val group = FuncGroup().apply { name = "Hello!" }
            insert(group)

            val person1 = randomPerson().apply { name = "Carol" }
            person1.groups.add(group)
            insert(person1)

            val person2 = randomPerson().apply { name = "Bob" }
            person2.groups.add(group)
            insert(person2)

            val columns = arrayOf(FuncPerson.NAME.`as`("personName"), FuncGroup.NAME.`as`("groupName"))
            val rows = select(*columns).where(FuncPerson.ID.eq(person1.id))
                .union()
                .select(*columns).where(FuncPerson.ID.eq(person2.id))
                .orderBy(FuncPerson.NAME.`as`("personName"))
                .get()
                .toList()

            assertThat(rows).hasSize(2)
            assertThat(rows[0].get<String>("personName")).isEqualTo("Bob")
            assertThat(rows[0].get<String>("groupName")).isEqualTo("Hello!")

            assertThat(rows[1].get<String>("personName")).isEqualTo("Carol")
            assertThat(rows[1].get<String>("groupName")).isEqualTo("Hello!")
        }
    }

    @Test
    fun `voilate unique constraint`() {
        assertThatThrownBy {
            with(requeryKotlin) {

                val uuid = UUID.randomUUID()
                val p1 = randomPerson().also { it.uuid = uuid }
                insert(p1)

                val p2 = randomPerson().also { it.uuid = uuid }
                insert(p2)
            }
        }.isInstanceOf(PersistenceException::class.java)
    }
}