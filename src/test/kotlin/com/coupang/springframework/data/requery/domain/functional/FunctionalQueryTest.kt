package com.coupang.springframework.data.requery.domain.functional

import com.coupang.kotlinx.logging.KLogging
import com.coupang.springframework.data.requery.domain.AbstractDomainTest
import com.coupang.springframework.data.requery.domain.functional.RandomData.randomPerson
import io.requery.query.Result
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

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
        requeryKtTmpl.deleteAll(FuncAddress::class)
        requeryKtTmpl.deleteAll(FuncGroup::class)
        requeryKtTmpl.deleteAll(FuncPhone::class)
        requeryKtTmpl.deleteAll(FuncPerson::class)
    }

    @Test
    fun `delete cascade one to one`() {
        val address = RandomData.randomAddress()
        requeryKtTmpl.insert(address)

        assertThat(address.id).isNotNull()
        val addressId = address.id!!

        val person = randomPerson()
        person.address = address

        requeryKtTmpl.insert(person)
        requeryKtTmpl.delete(person)

        assertThat(address.person).isNull()
        assertThat(requeryKtTmpl.findById(FuncAddress::class, addressId)).isNull()
    }

    @Test
    fun `delete one`() {
        val person = randomPerson()

        requeryKtTmpl.insert(person)
        assertThat(person.id).isNotNull()

        requeryKtTmpl.delete(person)

        assertThat(requeryKtTmpl.findById(FuncPerson::class, person.id)).isNull()
    }

    @Test
    fun `delete cascade remove one to many`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone1 = RandomData.randomPhone().apply { owner = person }
        val phone2 = RandomData.randomPhone().apply { owner = person }

        requeryKtTmpl.insert(phone1)
        requeryKtTmpl.insert(phone2)
        requeryKtTmpl.refresh(person)

        assertThat(person.phoneNumberList).containsOnly(phone1, phone2)

        requeryKtTmpl.delete(phone1)
        assertThat(person.phoneNumberList).containsOnly(phone2)
    }

    @Test
    fun `delete cascade one to many`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone1 = RandomData.randomPhone().apply { owner = person }
        requeryKtTmpl.insert(phone1)
        val phoneId = phone1.id

        assertThat(person.phoneNumbers).hasSize(1)
        requeryKtTmpl.delete(person)

        assertThat(requeryKtTmpl.findById(FuncPhone::class, phoneId)).isNull()
    }

    @Test
    fun `delete one to many result`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone1 = RandomData.randomPhone().apply { owner = person }
        val phone2 = RandomData.randomPhone().apply { owner = person }
        requeryKtTmpl.insertAll(listOf(phone1, phone2))
        requeryKtTmpl.refresh(person)

        assertThat(person.phoneNumbers.toList()).hasSize(2)

        requeryKtTmpl.deleteAll(person.phoneNumbers)

        assertThat(requeryKtTmpl.count(FuncPhone::class)).isEqualTo(0)
    }

    @Test
    fun `insert one to many`() {

        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone1 = RandomData.randomPhone().apply { owner = person }
        val phone2 = RandomData.randomPhone().apply { owner = person }
        requeryKtTmpl.insertAll(listOf(phone1, phone2))

        val set = person.phoneNumbers.toSet()
        assertThat(set).hasSize(2).containsOnly(phone1, phone2)
    }

    @Test
    fun `insert one to many inverse update`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone1 = RandomData.randomPhone()
        val phone2 = RandomData.randomPhone()
        person.phoneNumbers.add(phone1)
        person.phoneNumbers.add(phone2)

        requeryKtTmpl.update(person)

        val set = person.phoneNumbers.toSet()
        assertThat(set).hasSize(2).containsOnly(phone1, phone2)
        assertThat(phone1.owner).isEqualTo(person)
        assertThat(phone2.owner).isEqualTo(person)
    }

    @Test
    fun `insert one to many inverse`() {
        val person = randomPerson()
        val phone1 = RandomData.randomPhone()
        val phone2 = RandomData.randomPhone()
        phone1.owner = person
        person.phoneNumbers.add(phone1)
        person.phoneNumbers.add(phone2)

        requeryKtTmpl.insert(person)

        val set = person.phoneNumbers.toSet()
        assertThat(set).hasSize(2).containsOnly(phone1, phone2)
        assertThat(phone1.owner).isEqualTo(person)
        assertThat(phone2.owner).isEqualTo(person)
    }

    @Test
    fun `insert one to many inverse through set`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val phone1 = RandomData.randomPhone()
        val phone2 = RandomData.randomPhone()

        person.phoneNumbers.add(phone1)
        person.phoneNumbers.add(phone2)
        requeryKtTmpl.update(person)

        assertThat(person.phoneNumbers).hasSize(2).containsOnly(phone1, phone2)
    }

    @Test
    fun `insert one to many insert`() {
        val person = randomPerson()
        val phone1 = RandomData.randomPhone()
        val phone2 = RandomData.randomPhone()
        person.phoneNumbers.add(phone1)
        person.phoneNumbers.add(phone2)

        requeryKtTmpl.insert(person)

        val set = person.phoneNumbers.toSet()
        assertThat(set).hasSize(2).containsOnly(phone1, phone2)

        assertThat(requeryKtTmpl.select(FuncPhone::class).get().toList().size).isEqualTo(2)
        assertThat(requeryKtTmpl.count(FuncPhone::class)).isEqualTo(2)
    }

    @Test
    fun `insert one to many insert through list`() {
        val person = randomPerson()
        val phone1 = RandomData.randomPhone()
        val phone2 = RandomData.randomPhone()
        person.phoneNumbers.add(phone1)
        person.phoneNumbers.add(phone2)

        requeryKtTmpl.insert(person)

        val set = person.phoneNumberList.toSet()
        assertThat(set).hasSize(2).containsOnly(phone1, phone2)
    }

    @Test
    fun `many to one refresh`() {
        val person = randomPerson()
        val phone1 = RandomData.randomPhone()
        val phone2 = RandomData.randomPhone()
        person.phoneNumbers.add(phone1)
        person.phoneNumbers.add(phone2)

        requeryKtTmpl.insert(person)

        assertThat(phone1.owner).isEqualTo(person)
        assertThat(phone2.owner).isEqualTo(person)

        requeryKtTmpl.refresh(phone1, FuncPhone.OWNER)
        requeryKtTmpl.refresh(phone2, FuncPhone.OWNER)
    }

    @Test
    fun `insert many to many`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)
        assertThat(person.groups.toList()).isEmpty()

        val addedGroups = mutableListOf<FuncGroup>()

        requeryKtTmpl.withTransaction {
            for(i in 0 until 10) {
                val group = FuncGroup().apply {
                    name = "Group$i"
                    description = "Some description"
                    type = GroupType.PRIVATE
                }
                insert(group)
                person.groups.add(group)
                addedGroups += group
            }
            update(person)
        }

        requeryKtTmpl.refresh(person, FuncPerson.GROUPS)
        addedGroups.forEach { group ->
            assertThat(group.members.toList()).contains(person)
        }
    }

    @Test
    fun `insert many to many self referencing`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val addedPeople = mutableListOf<FuncPerson>()

        for(i in 0 until 10) {
            val p = randomPerson()
            person.friends.add(p)
            addedPeople += p
        }

        requeryKtTmpl.update(person)

        assertThat(person.friends).containsAll(addedPeople)
        assertThat(requeryKtTmpl.count(FuncPerson::class)).isEqualTo(11)
    }

    @Test
    fun `iterate insert many`() {
        val person = randomPerson()
        assertThat(person.groups).isEmpty()

        val toAdd = mutableSetOf<FuncGroup>()

        for(i in 0 until 10) {
            val group = FuncGroup().apply { name = "Group$i" }
            person.groups.add(group)
            toAdd += group
        }

        var count = 0
        person.groups.forEach {
            assertThat(toAdd.contains(it)).isTrue()
            count++
        }
        assertThat(count).isEqualTo(10)
        requeryKtTmpl.insert(person)
    }

    @Test
    fun `delete many to many`() {
        val person = randomPerson()
        requeryKtTmpl.insert(person)

        val groups = mutableSetOf<FuncGroup>()

        requeryKtTmpl.withTransaction {
            for(i in 0 until 10) {
                val group = FuncGroup().apply { "DeleteGroup$i" }
                insert(group)
                person.groups.add(group)
                groups += group
            }
            update(person)
        }
        groups.forEach {
            person.groups.remove(it)
        }
        requeryKtTmpl.update(person)
        assertThat(person.groups.toList()).isEmpty()

        // many to many 관계를 끊은 것이므로 group 은 삭제되지 않는다.
        assertThat(requeryKtTmpl.count(FuncGroup::class)).isEqualTo(10)
    }

    @Test
    fun `many to many order by`() {
        val group = FuncGroup().apply { name = "Group" }
        requeryKtTmpl.insert(group)

        for(i in 3 downTo 0) {
            val person = randomPerson()
            person.name = (65 + i).toChar().toString()
            requeryKtTmpl.insert(person)
            group.owners.add(person)
        }
        requeryKtTmpl.update(group)
        requeryKtTmpl.refresh(group, FuncGroup.OWNERS)

        val owners = group.owners.toList()

        assertThat(owners).hasSize(4)
        assertThat(owners[0].name).isEqualTo("A")
        assertThat(owners[1].name).isEqualTo("B")
        assertThat(owners[2].name).isEqualTo("C")
        assertThat(owners[3].name).isEqualTo("D")
    }

    @Test
    fun `query function now`() {
        val person = randomPerson()
        person.birthday = LocalDate.now().plusDays(1)
        requeryKtTmpl.insert(person)

        val result = requeryKtTmpl.select(FuncPerson::class)
            .where(FuncPerson.BIRTHDAY.gt(LocalDate.now()))
            .get()

        assertThat(result.toList()).hasSize(1)
    }

    @Test
    fun `query function random`() {
        List(10) {
            requeryKtTmpl.insert(randomPerson())
        }

        val result = requeryKtTmpl.select(FuncPerson::class)
            .orderBy(io.requery.query.function.Random())
            .get()

        assertThat(result.toList()).hasSize(10)
    }

    @Test fun `single query where`() {
        val name = "duplicateFirstname"

        List(10) {
            requeryKtTmpl.insert(randomPerson().also { it.name = name })
        }

        val result = requeryKtTmpl.select(FuncPerson::class)
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
            requeryKtTmpl.insert(person)
        }

        // TODO: not 연산자가 제대로 동작하지 않는다. (Java 와 Kotlin 모두)
        val result = requeryKtTmpl.select(FuncPerson::class)
            .where(
                FuncPerson.NAME.ne(name).and(FuncPerson.EMAIL.ne(email))
            )
            .get()

        assertThat(result.toList()).hasSize(8)
    }

    @Test
    fun `single query execute`() {
        requeryKtTmpl.insertAll(RandomData.randomPersons(10))

        val result: Result<FuncPerson> = requeryKtTmpl.select(FuncPerson::class).get()
        assertThat(result.toList()).hasSize(10)

        val person = randomPerson()
        requeryKtTmpl.insert(person)

        // 이렇게 Result<T> 로 얻은 객체는 조건을 가진 Query 구문이라고 봐도 될 듯
        assertThat(result.toList()).hasSize(11)
    }

    @Test
    fun `single query with limit and offset`() {
        val name = "duplicateFirstName"
        List(10) {
            requeryKtTmpl.insert(randomPerson().also { it.name = name })
        }

        for(i in 0 until 3) {
            val query = requeryKtTmpl
                .select(FuncPerson::class)
                .where(FuncPerson.NAME.eq(name))
                .orderBy(FuncPerson.NAME)
                .limit(5)
                .get()
            assertThat(query.toList()).hasSize(5)

            val query2 = requeryKtTmpl
                .select(FuncPerson::class)
                .where(FuncPerson.NAME.eq(name))
                .limit(5)
                .offset(5)
                .get()

            assertThat(query2.toList()).hasSize(5)
        }
    }
}