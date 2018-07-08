package org.springframework.data.requery.domain.functional;

import io.requery.PersistenceException;
import io.requery.meta.NumericAttribute;
import io.requery.query.*;
import io.requery.query.function.Case;
import io.requery.query.function.Coalesce;
import io.requery.query.function.Count;
import io.requery.query.function.Random;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.requery.domain.AbstractDomainTest;
import org.springframework.data.requery.domain.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Diego on 2018. 6. 9..
 */
@Slf4j
public class FunctionalQueryTest extends AbstractDomainTest {

    private static final int COUNT = 100;

    @Before
    public void setup() {
        requeryTemplate.deleteAll(Address.class);
        requeryTemplate.deleteAll(Group.class);
        requeryTemplate.deleteAll(Phone.class);
        requeryTemplate.deleteAll(Person.class);
    }

    @Test
    public void query_function_now() {
        Person person = RandomData.randomPerson();
        person.setBirthday(LocalDate.now().plusDays(1));
        requeryTemplate.insert(person);

        Result<Person> result = requeryTemplate.select(Person.class)
            .where(Person.BIRTHDAY.gt(LocalDate.now()))
            .get();

        assertThat(result.toList()).hasSize(1);
    }

    @Test
    public void query_function_random() {
        Set<Person> people = RandomData.randomPeople(10);
        requeryTemplate.insertAll(people);

        Result<Person> result = requeryTemplate.select(Person.class)
            .orderBy(new Random())
            .get();

        assertThat(result.toList()).hasSize(10);
    }

    @Test
    public void single_query_where() {
        String name = "duplicateFirstMame";

        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            person.setName(name);
            requeryTemplate.insert(person);
        }

        Result<Person> result = requeryTemplate.select(Person.class)
            .where(Person.NAME.eq(name))
            .get();

        assertThat(result.toList()).hasSize(10);
    }

    @Test
    public void single_query_where_not() {
        String name = "firstName";
        String email = "not@test.io";

        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            switch (i) {
                case 0:
                    person.setName(name);
                    break;
                case 1:
                    person.setEmail(email);
                    break;
            }
            requeryTemplate.insert(person);
        }

        // FIXME: not 연산자가 제대로 동작하지 않는다. (Java 와 Kotlin 모두)
        //
        Result<Person> result = requeryTemplate.select(Person.class)
            .where(Person.NAME.ne(name).and(Person.EMAIL.ne(email)))
            .get();

        assertThat(result.toList()).hasSize(8);
    }

    @Test
    public void single_query_execute() {
        requeryTemplate.insertAll(RandomData.randomPeople(10));

        Result<Person> result = requeryTemplate.select(Person.class).get();
        assertThat(result.toList()).hasSize(10);

        Person person = RandomData.randomPerson();
        requeryTemplate.insert(person);

        assertThat(result.toList()).hasSize(11);
    }

    @Test
    public void single_query_with_limit_and_offset() {
        String name = "duplicateFirstName";

        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            person.setName(name);
            requeryTemplate.insert(person);
        }

        for (int i = 0; i < 3; i++) {
            Result<Person> query = requeryTemplate.select(Person.class)
                .where(Person.NAME.eq(name))
                .orderBy(Person.NAME)
                .limit(5)
                .get();
            assertThat(query.toList()).hasSize(5);

            Result<Person> query2 = requeryTemplate.select(Person.class)
                .where(Person.NAME.eq(name))
                .limit(5)
                .offset(5)
                .get();
            assertThat(query2.toList()).hasSize(5);
        }
    }

    @Test
    public void single_query_where_null() {
        Person person = RandomData.randomPerson();
        person.setName(null);
        requeryTemplate.insert(person);

        Result<Person> query = requeryTemplate.select(Person.class)
            .where(Person.NAME.isNull())
            .get();

        assertThat(query.toList()).hasSize(1);
    }

    @Test
    public void delete_all() {
        String name = "someName";

        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            person.setName(name);
            requeryTemplate.insert(person);
        }

        assertThat(requeryTemplate.deleteAll(Person.class)).isGreaterThan(0);
        assertThat(requeryTemplate.select(Person.class).get().firstOrNull()).isNull();
    }

    @Test
    public void delete_batch() {
        Set<Person> people = RandomData.randomPeople(COUNT);
        requeryTemplate.insertAll(people);

        assertThat(requeryTemplate.count(Person.class).get().value()).isEqualTo(people.size());

        requeryTemplate.deleteAll(people);
        assertThat(requeryTemplate.count(Person.class).get().value()).isEqualTo(0);
    }

    @Test
    public void query_by_foreign_key() {
        Person person = RandomData.randomPerson();
        requeryTemplate.insert(person);

        Phone phone1 = RandomData.randomPhone();
        Phone phone2 = RandomData.randomPhone();
        person.getPhoneNumbers().add(phone1);
        person.getPhoneNumbers().add(phone2);

        requeryTemplate.upsert(person);
        assertThat(person.getPhoneNumberSet()).containsOnly(phone1, phone2);

        // by entity
        Result<Phone> query1 = requeryTemplate.select(Phone.class).where(Phone.OWNER.eq(person)).get();

        assertThat(query1.toList()).hasSize(2).containsOnly(phone1, phone2);
        assertThat(person.getPhoneNumberList()).hasSize(2).containsAll(query1.toList());

        // by id
        Result<Phone> query2 = requeryTemplate.select(Phone.class).where(Phone.OWNER_ID.eq(person.getId())).get();

        assertThat(query2.toList()).hasSize(2).containsOnly(phone1, phone2);
        assertThat(person.getPhoneNumberList()).hasSize(2).containsAll(query2.toList());
    }

    @Test
    public void query_by_UUID() {
        Person person = RandomData.randomPerson();
        requeryTemplate.insert(person);

        UUID uuid = person.getUUID();
        Person loaded = requeryTemplate.select(Person.class).where(Person.UUID.eq(uuid)).get().first();
        assertThat(loaded).isEqualTo(person);
    }

    @Test
    public void query_select_distinct() {
        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            person.setName(Integer.toString(i / 2));
            requeryTemplate.insert(person);
        }

        Result<Tuple> result = requeryTemplate.select(Person.NAME).distinct().get();

        assertThat(result.toList()).hasSize(5);
    }

    @Test
    public void query_select_count() {
        Set<Person> people = RandomData.randomPeople(10);
        requeryTemplate.insertAll(people);

        Result<Tuple> result = requeryTemplate.select(Count.count(Person.class).as("bb")).get();
        assertThat(result.first().<Integer>get("bb")).isEqualTo(people.size());

        Result<Tuple> result2 = requeryTemplate.select(Count.count(Person.class)).get();
        assertThat(result2.first().<Integer>get(0)).isEqualTo(people.size());

        assertThat(requeryTemplate.count(Person.class).get().value()).isEqualTo(people.size());

        requeryTemplate.count(Person.class).get().consume(
            count -> assertThat(count).isEqualTo(people.size())
        );
    }

    @Test
    public void query_select_count_where() {
        Person person = RandomData.randomPerson();
        person.setName("countMe");
        requeryTemplate.insert(person);
        requeryTemplate.insertAll(RandomData.randomPeople(9));

        assertThat(requeryTemplate.count(Person.class).where(Person.NAME.eq("countMe")).get().value()).isEqualTo(1);

        Result<Tuple> result = requeryTemplate.select(Count.count(Person.class).as("cnt"))
            .where(Person.NAME.eq("countMe"))
            .get();

        assertThat(result.first().<Integer>get("cnt")).isEqualTo(1);
    }

    @Test
    public void query_not_null() throws Exception {
        requeryTemplate.insertAll(RandomData.randomPeople(10));

        Thread.sleep(10L);

        Result<Person> result = requeryTemplate.select(Person.class).where(Person.NAME.notNull()).get();
        assertThat(result.toList()).hasSize(10);
    }

    @Test
    public void query_from_sub_query() {
        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            person.setAge(i + 1);
            requeryTemplate.insert(person);
        }

        NumericAttribute<Person, Integer> personAge = Person.AGE;

        Return<? extends Result<Tuple>> subQuery = requeryTemplate.select(personAge.sum().as("avg_age"))
            .from(Person.class)
            .groupBy(personAge)
            .as("sums");

        Result<Tuple> result =
            requeryTemplate.select(NamedNumericExpression.ofInteger("avg_age").avg())
                .from(subQuery)
                .get();

        assertThat(result.first().<Integer>get(0)).isGreaterThanOrEqualTo(5);
    }

    @Test
    public void query_join_orderBy() {
        Person person = RandomData.randomPerson();
        person.setAddress(RandomData.randomAddress());
        requeryTemplate.insert(person);

        // not a useful query just tests the sql output
        Result<Address> result = requeryTemplate.select(Address.class)
            .join(Person.class).on(Person.ADDRESS_ID.eq(Address.ID))
            .where(Person.ID.eq(person.getId()))
            .orderBy(Address.CITY.desc())
            .get();

        List<Address> addresses = result.toList();
        assertThat(addresses.size()).isGreaterThan(0);
    }

    @Test
    public void query_select_min() {
        for (int i = 0; i < 9; i++) {
            requeryTemplate.insert(RandomData.randomPerson());
        }
        Person person = RandomData.randomPerson();
        person.setBirthday(LocalDate.of(1800, 11, 11));
        requeryTemplate.insert(person);

        Result<Tuple> query = requeryTemplate.select(Person.BIRTHDAY.min().as("oldestBDay")).get();
        LocalDate birthday = query.first().get("oldestBDay");
        assertThat(birthday).isEqualTo(LocalDate.of(1800, 11, 11));
    }

    @Test
    public void query_select_trim() {
        Person person = RandomData.randomPerson();
        person.setName("  Name  ");
        requeryTemplate.insert(person);

        Tuple result = requeryTemplate.select(Person.NAME.trim().as("name")).get().first();
        String name = result.get(0);
        assertThat(name).isEqualTo("Name");
    }

    @Test
    public void query_select_substr() {
        Person person = RandomData.randomPerson();
        person.setName("  Name");
        requeryTemplate.insert(person);

        Tuple result = requeryTemplate.select(Person.NAME.substr(3, 6).as("name")).get().first();
        String name = result.get(0);
        assertThat(name).isEqualTo("Name");
    }

    @Test
    public void query_orderBy() {
        for (int i = 0; i < 10; i++) {
            Person person = RandomData.randomPerson();
            person.setAge(i);
            requeryTemplate.insert(person);
        }

        Result<Tuple> query = requeryTemplate.select(Person.AGE).orderBy(Person.AGE.desc()).get();

        int topAge = query.first().<Integer>get(0);
        assertThat(topAge).isEqualTo(9);
    }

    @Test
    public void query_orderBy_function() {
        Person person1 = RandomData.randomPerson();
        person1.setName("BOBB");
        requeryTemplate.insert(person1);
        Person person2 = RandomData.randomPerson();
        person2.setName("BobA");
        requeryTemplate.insert(person2);
        Person person3 = RandomData.randomPerson();
        person3.setName("bobC");
        requeryTemplate.insert(person3);

        List<Tuple> people = requeryTemplate.select(Person.NAME)
            .orderBy(Person.NAME.upper().desc())
            .get()
            .toList();

        assertThat(people).hasSize(3);
        assertThat(people.get(0).<String>get(0)).isEqualTo("bobC");
        assertThat(people.get(1).<String>get(0)).isEqualTo("BOBB");
        assertThat(people.get(2).<String>get(0)).isEqualTo("BobA");
    }

    @Test
    public void query_groupBy() {
        for (int i = 0; i < 5; i++) {
            Person person = RandomData.randomPerson();
            person.setAge(i);
            requeryTemplate.insert(person);
        }

        Result<Tuple> result = requeryTemplate.select(Person.AGE)
            .groupBy(Person.AGE)
            .having(Person.AGE.gt(3))
            .get();

        assertThat(result.toList()).hasSize(1);

        Result<Tuple> result2 = requeryTemplate.select(Person.AGE)
            .groupBy(Person.AGE)
            .having(Person.AGE.lt(0))
            .get();

        assertThat(result2.toList()).isEmpty();
    }

    @Test
    public void query_select_where_in() {
        String name = "Hello!";
        Person person = RandomData.randomPerson();
        person.setName(name);
        requeryTemplate.insert(person);

        Group group = new Group();
        group.setName(name);
        requeryTemplate.insert(group);

        person.getGroups().add(group);
        requeryTemplate.upsert(group);

        WhereAndOr<? extends Result<Tuple>> groupNames = requeryTemplate.select(Group.NAME)
            .where(Group.NAME.eq(name));

        Person p = requeryTemplate.select(Person.class)
            .where(Person.NAME.in(groupNames)).get().first();
        assertThat(p.getName()).isEqualTo(name);

        p = requeryTemplate.select(Person.class)
            .where(Person.NAME.notIn(groupNames)).get().firstOrNull();
        assertThat(p).isNull();

        p = requeryTemplate.select(Person.class)
            .where(Person.NAME.in(Arrays.asList("Hello!", "Other"))).get().first();
        assertThat(p.getName()).isEqualTo(name);

        p = requeryTemplate.select(Person.class)
            .where(Person.NAME.in(Collections.singletonList("Hello!"))).get().first();
        assertThat(p.getName()).isEqualTo(name);

        p = requeryTemplate.select(Person.class)
            .where(Person.NAME.notIn(Collections.singletonList("Hello!"))).get().firstOrNull();
        assertThat(p).isNull();
    }

    @Test
    public void query_between() {
        Person person = RandomData.randomPerson();
        person.setAge(75);
        requeryTemplate.insert(person);

        Person p = requeryTemplate.select(Person.class).where(Person.AGE.between(50, 100)).get().first();
        assertThat(p).isEqualTo(person);
    }

    @Test
    public void query_conditions() {
        Person person = RandomData.randomPerson();
        person.setAge(75);
        requeryTemplate.insert(person);

        Person p = requeryTemplate.select(Person.class).where(Person.AGE.gte(75)).get().first();
        assertThat(p).isEqualTo(person);

        p = requeryTemplate.select(Person.class).where(Person.AGE.lte(75)).get().first();
        assertThat(p).isEqualTo(person);

        p = requeryTemplate.select(Person.class).where(Person.AGE.gt(75)).get().firstOrNull();
        assertThat(p).isNull();

        p = requeryTemplate.select(Person.class).where(Person.AGE.lt(75)).get().firstOrNull();
        assertThat(p).isNull();

        p = requeryTemplate.select(Person.class).where(Person.AGE.ne(75)).get().firstOrNull();
        assertThat(p).isNull();
    }

    @Test
    public void query_compound_conditions() {
        Person person1 = RandomData.randomPerson();
        person1.setAge(75);
        requeryTemplate.insert(person1);

        Person person2 = RandomData.randomPerson();
        person2.setAge(10);
        person2.setName("Carol");
        requeryTemplate.insert(person2);

        Person person3 = RandomData.randomPerson();
        person3.setAge(0);
        person3.setName("Bob");
        requeryTemplate.insert(person3);

        Result<Person> result = requeryTemplate.select(Person.class)
            .where(Person.AGE.gt(5).and(Person.AGE.lt(75))).and(Person.NAME.ne("Bob"))
//            .where(Person.AGE.gt(5).and(Person.AGE.lt(75)).and(Person.NAME.ne("Bob"))) // TODO?: Error occurs
            .or(Person.NAME.eq("Bob"))
            .get();

        assertThat(result.toList()).hasSize(2).containsOnly(person2, person3);

        result = requeryTemplate.select(Person.class)
            .where(Person.AGE.gt(5).or(Person.AGE.lt(75)))
            .and(Person.NAME.eq("Bob"))
            .get();

        assertThat(result.toList()).hasSize(1).containsOnly(person3);
    }

    @Test
    public void query_consume() {
        requeryTemplate.insertAll(RandomData.randomPeople(10));

        List<Person> people = new ArrayList<>();
        Result<Person> result = requeryTemplate.select(Person.class).get();
        result.each(people::add);

        assertThat(people).hasSize(10);
    }

    @Test
    public void query_map() {
        Person person = RandomData.randomPerson();
        person.setEmail("one@test.com");
        requeryTemplate.insert(person);
        requeryTemplate.insertAll(RandomData.randomPeople(9));

        Result<Person> result = requeryTemplate.select(Person.class).get();
        Map<String, Person> map = result.toMap(Person.EMAIL, new ConcurrentHashMap<>());
        assertThat(map.get("one@test.com")).isNotNull();

        map = result.toMap(Person.EMAIL);
        assertThat(map.get("one@test.com")).isNotNull();

        Map<String, Person> jmap = result.toList().stream().collect(toMap(Person::getEmail, Function.identity()));
        assertThat(jmap.get("one@test.com")).isNotNull();
    }

    @Test
    public void query_update() {
        Person person = RandomData.randomPerson();
        person.setAge(100);
        requeryTemplate.insert(person);

        int updatedCount = requeryTemplate.update(Person.class)
            .set(Person.ABOUT, "nothing")
            .set(Person.AGE, 50)
            .where(Person.AGE.eq(100))
            .get()
            .value();

        assertThat(updatedCount).isEqualTo(1);
    }

    @Test
    public void query_update_refresh() {
        Person person = RandomData.randomPerson();
        person.setAge(100);
        requeryTemplate.insert(person);

        int updatedCount = requeryTemplate.update(Person.class)
            .set(Person.AGE, 50)
            .where(Person.ID.eq(person.getId()))
            .get()
            .value();

        assertThat(updatedCount).isEqualTo(1);

        Person selected = requeryTemplate.select(Person.class).where(Person.ID.eq(person.getId())).get().first();
        assertThat(selected.getAge()).isEqualTo(50);
    }

    @Test
    public void query_coalesce() {
        Person person = RandomData.randomPerson();
        person.setName("Carol");
        person.setEmail(null);
        requeryTemplate.insert(person);

        person = RandomData.randomPerson();
        person.setName("Bob");
        person.setEmail("test@test.com");
        person.setHomepage(null);
        requeryTemplate.insert(person);

        Result<Tuple> result = requeryTemplate.select(Coalesce.coalesce(Person.EMAIL, Person.NAME)).get();
        List<Tuple> list = result.toList();
        List<String> values = list.stream().map(it -> it.<String>get(0)).collect(Collectors.toList());

        assertThat(values).hasSize(2).containsOnly("Carol", "test@test.com");
    }

    @Test
    public void query_like() {
        Person person1 = RandomData.randomPerson();
        person1.setName("Carol");
        requeryTemplate.insert(person1);
        Person person2 = RandomData.randomPerson();
        person2.setName("Bob");
        requeryTemplate.insert(person2);

        Person p = requeryTemplate.select(Person.class)
            .where(Person.NAME.like("B%"))
            .get()
            .first();

        assertThat(p.getName()).isEqualTo("Bob");

        p = requeryTemplate.select(Person.class)
            .where(Person.NAME.lower().like("b%"))
            .get()
            .first();

        assertThat(p.getName()).isEqualTo("Bob");

        Person p2 = requeryTemplate.select(Person.class)
            .where(Person.NAME.notLike("B%"))
            .get()
            .firstOrNull();

        assertThat(p2.getName()).isEqualTo("Carol");
    }

    @Test
    public void query_equals_ignore_case() {
        Person person = RandomData.randomPerson();
        person.setName("Carol");
        requeryTemplate.insert(person);

        Person p = requeryTemplate.select(Person.class)
            .where(Person.NAME.equalsIgnoreCase("carol"))
            .get()
            .first();

        assertThat(p).isEqualTo(person);
    }

    @Test
    public void query_case() {
        List<String> names = Arrays.asList("Carol", "Bob", "Jack");
        names.forEach(name -> {
            Person person = RandomData.randomPerson();
            person.setName(name);
            requeryTemplate.insert(person);
        });

        Result<Tuple> a = requeryTemplate.select(
            Person.NAME,
            Case.type(String.class)
                .when(Person.NAME.eq("Bob"), "B")
                .when(Person.NAME.eq("Carol"), "C")
                .elseThen("Unknown")
        )
            .from(Person.class)
            .orderBy(Person.NAME)
            .get();

        List<Tuple> list = a.toList();
        assertThat(list.get(0).<String>get(1)).isEqualTo("B");
        assertThat(list.get(1).<String>get(1)).isEqualTo("C");
        assertThat(list.get(2).<String>get(1)).isEqualTo("Unknown");

        a = requeryTemplate.select(
            Person.NAME,
            Case.type(Integer.class)
                .when(Person.NAME.eq("Bob"), 1)
                .when(Person.NAME.eq("Carol"), 2)
                .elseThen(0)
        )
            .orderBy(Person.NAME)
            .get();

        list = a.toList();
        assertThat(list.get(0).<Integer>get(1)).isEqualTo(1);
        assertThat(list.get(1).<Integer>get(1)).isEqualTo(2);
        assertThat(list.get(2).<Integer>get(1)).isEqualTo(0);
    }

    @Test
    public void query_union() {
        Person person = RandomData.randomPerson();
        person.setName("Carol");
        requeryTemplate.insert(person);

        Group group = new Group();
        group.setName("Hello!");
        requeryTemplate.insert(group);

        // select name as name from FuncPerson
        // union
        // select name as name from FuncGroup order by name
        List<Tuple> result = requeryTemplate.select(Person.NAME.as("name"))
            .union()
            .select(Group.NAME.as("name"))
            .orderBy(Group.NAME.as("name"))
            .get()
            .toList();

        assertThat(result.get(0).<String>get(0)).isEqualTo("Carol");
        assertThat(result.get(1).<String>get(0)).isEqualTo("Hello!");
    }

    @Test
    public void query_raw() {
        int count = 5;

        List<Person> people = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            people.add(requeryTemplate.insert(RandomData.randomPerson()));
        }

        List<Long> resultIds = new ArrayList<>();

        Result<Tuple> result = requeryTemplate.raw("select * from Person");
        List<Tuple> rows = result.toList();
        assertThat(rows).hasSize(count);

        for (int index = 0; index < rows.size(); index++) {
            Tuple row = rows.get(index);
            String name = row.get("name");
            assertThat(name).isEqualTo(people.get(index).getName());
            Long id = row.<Long>get("personId");
            assertThat(id).isEqualTo(people.get(index).getId());
            resultIds.add(id);
        }

        result = requeryTemplate.raw("select * from Person WHERE personId in ?", resultIds);
        rows = result.toList();
        List<Long> ids = rows.stream().map(it -> it.<Long>get("personId")).collect(Collectors.toList());
        assertThat(ids).isEqualTo(resultIds);

        result = requeryTemplate.raw("select count(*) from Person");
        int number = result.first().<Number>get(0).intValue();
        assertThat(number).isEqualTo(count);

        result = requeryTemplate.raw("select * from Person WHERE personId = ?", people.get(0));
        assertThat(result.first().<Long>get("personId")).isEqualTo(people.get(0).getId());
    }

    @Test
    public void query_raw_entities() {
        int count = 5;

        List<Person> people = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            people.add(requeryTemplate.insert(RandomData.randomPerson()));
        }

        List<Long> resultIds = new ArrayList<>();

        Result<Person> result = requeryTemplate.raw(Person.class, "select * from Person");
        List<Person> rows = result.toList();
        assertThat(rows).hasSize(count);

        for (int index = 0; index < rows.size(); index++) {
            Person row = rows.get(index);
            String name = row.getName();
            assertThat(name).isEqualTo(people.get(index).getName());
            Long id = row.getId();
            assertThat(id).isEqualTo(people.get(index).getId());
            resultIds.add(id);
        }

        result = requeryTemplate.raw(Person.class, "select * from Person WHERE personId in ?", resultIds);
        rows = result.toList();
        List<Long> ids = rows.stream().map(Person::getId).collect(Collectors.toList());
        assertThat(ids).isEqualTo(resultIds);

        result = requeryTemplate.raw(Person.class, "select * from Person WHERE personId = ?", people.get(0));
        assertThat(result.first().getId()).isEqualTo(people.get(0).getId());
    }

    @Test
    public void query_union_join_on_same_entities() {
        Group group = new Group();
        group.setName("Hello!");
        requeryTemplate.insert(group);

        Person person1 = RandomData.randomPerson();
        person1.setName("Carol");
        person1.getGroups().add(group);
        requeryTemplate.insert(person1);

        Person person2 = RandomData.randomPerson();
        person2.setName("Bob");
        person2.getGroups().add(group);
        requeryTemplate.insert(person2);

        Expression[] columns = { Person.NAME.as("personName"), Group.NAME.as("GroupName") };
        List<Tuple> rows = requeryTemplate.select(columns).where(Person.ID.eq(person1.getId()))
            .union()
            .select(columns).where(Person.ID.eq(person2.getId()))
            .orderBy(Person.NAME.as("personName"))
            .get()
            .toList();

        assertThat(rows).hasSize(2);
        assertThat(rows.get(0).<String>get("personName")).isEqualTo("Bob");
        assertThat(rows.get(0).<String>get("groupName")).isEqualTo("Hello!");
        assertThat(rows.get(1).<String>get("personName")).isEqualTo("Carol");
        assertThat(rows.get(1).<String>get("groupName")).isEqualTo("Hello!");
    }

    @Test
    public void violate_unique_constraint() {
        assertThatThrownBy(() -> {
            UUID uuid = UUID.randomUUID();
            Person p1 = RandomData.randomPerson();
            p1.setUUID(uuid);
            requeryTemplate.insert(p1);

            Person p2 = RandomData.randomPerson();
            p2.setUUID(uuid);
            requeryTemplate.insert(p2);
        }).isInstanceOf(PersistenceException.class);
    }
}
