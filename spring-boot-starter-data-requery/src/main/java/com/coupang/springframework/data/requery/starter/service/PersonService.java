package com.coupang.springframework.data.requery.starter.service;

import org.springframework.data.requery.starter.domain.model.AbstractPerson;
import org.springframework.data.requery.starter.domain.model.Person;
import org.springframework.data.requery.starter.domain.repository.PersonRepository;
import org.springframework.data.requery.starter.dto.PersonDto;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Diego on 2018. 7. 1..
 */
@Component
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        if (personRepository == null) {
            throw new NullPointerException("PersonRepository should not be null");
        }
        this.personRepository = personRepository;
    }

    @Nullable
    public PersonDto getPersonById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(AbstractPerson::toPersonDto).orElse(null);
    }

}
