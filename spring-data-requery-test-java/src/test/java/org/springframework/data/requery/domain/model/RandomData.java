package org.springframework.data.requery.domain.model;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Diego on 2018. 6. 13..
 */
@Slf4j
public class RandomData {

    private RandomData() {}

    private static Random rnd = new Random(System.currentTimeMillis());

    private static List<String> firstNames = Arrays.asList("Alice", "Bob", "Carol", "Debop", "Erik");
    private static List<String> lastNames = Arrays.asList("Smith", "Lee", "Jones", "Bae", "Kim");

    public static Person randomPerson() {
        Person person = new Person();
        person.setName(firstNames.get(rnd.nextInt(firstNames.size())) +
                       " " +
                       lastNames.get(rnd.nextInt(lastNames.size())));
        person.setEmail(person.getName().replace(" ", ".").toLowerCase() +
                        rnd.nextInt(1000) +
                        "@example.com");
        person.setUUID(UUID.randomUUID());
        try {
            person.setHomepage(new URL("http://www.requery.io"));
        } catch (MalformedURLException e) {
            log.error("URL error");
        }
        person.setBirthday(LocalDate.of(1900 + rnd.nextInt(90), rnd.nextInt(11) + 1, rnd.nextInt(20) + 1));

        return person;
    }

    public static Set<Person> randomPeople(int count) {
        Set<Person> persons = new HashSet<>();
        int cnt = 0;
        while (cnt < count) {
            persons.add(randomPerson());
            cnt ++;
        }
        return persons;
    }

    public static Address randomAddress() {
        Address addr = new Address();
        addr.setLine1(rnd.nextInt(4) + " Fake St");
        addr.setCity("San Francisco");
        addr.setState("CA");
        addr.setZip(Integer.toString((10000 + rnd.nextInt(70000))));
        addr.setType(AddressType.HOME);
        addr.setLatitude(0.0f);
        addr.setLongitude(0.0f);

        return addr;
    }

    public static Phone randomPhone() {
        Phone phone = new Phone();
        phone.setPhoneNumber("555-" + (1000 + rnd.nextInt(8000)));
        phone.setNormalized(true);
        ArrayList<Integer> extensions = new ArrayList<>(5);
        extensions.addAll(Arrays.asList(300, 301, 302, 303, 304));
        phone.setExtensions(extensions);

        return phone;
    }
}
