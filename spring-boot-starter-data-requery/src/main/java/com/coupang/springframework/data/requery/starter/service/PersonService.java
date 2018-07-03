package com.coupang.springframework.data.requery.starter.service;

import com.coupang.springframework.data.requery.starter.domain.model.AbstractPerson;
import com.coupang.springframework.data.requery.starter.domain.model.Person;
import com.coupang.springframework.data.requery.starter.domain.repository.PersonRepository;
import com.coupang.springframework.data.requery.starter.dto.PersonDto;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Diego on 2018. 7. 1..
 */
@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(@NotNull PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Nullable
    public PersonDto getPersonById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(AbstractPerson::toPersonDto).orElse(null);
    }

}
