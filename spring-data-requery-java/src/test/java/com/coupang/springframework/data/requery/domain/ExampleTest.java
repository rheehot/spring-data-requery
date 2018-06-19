package com.coupang.springframework.data.requery.domain;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.ExampleMatcher.matching;

public class ExampleTest {

    Person person;
    Example<Person> example;

    @Before
    public void setup() {
        person = new Person();
        person.setFirstname("rand");

        example = Example.of(person);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNullProbe() {
        Example.of(null);
    }

    @Test
    public void returnsSampleObjectsClassAsProbeType() {
        assertThat(example.getProbeType()).isEqualTo(Person.class);
    }

    @Test
    public void shouldCompareUsingHashCodeAndEquals() throws Exception {
        Example<Person> example = Example.of(person, matching().withIgnoreCase("firstname"));
        Example<Person> sameAsExample = Example.of(person, matching().withIgnoreCase("firstname"));

        Example<Person> different = Example.of(person, matching().withMatcher("firstname",
                                                                              GenericPropertyMatchers.contains()));

        assertThat(example.hashCode()).isEqualTo(sameAsExample.hashCode());
        assertThat(example.hashCode()).isNotEqualTo(different.hashCode());
        assertThat(example).isEqualTo(sameAsExample);
        assertThat(example).isNotEqualTo(different);
    }

    @Data
    static class Person {
        String firstname;
    }
}
