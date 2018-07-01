package com.coupang.springframework.data.requery.starter.api;

import com.coupang.springframework.data.requery.core.RequeryOperations;
import com.coupang.springframework.data.requery.starter.domain.model.Person;
import io.requery.query.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author Diego on 2018. 7. 1..
 */
@RestController
@RequestMapping("api/v1/person")
public class PersonController {

    @Inject
    private RequeryOperations requeryTemplate;

    // TODO DTO, repository, service 사용하기
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Person getPerson(@PathVariable("id") Long id) {
        if (id == null) {
            return null;
        }
        Person person = requeryTemplate.select(Person.class).where(Person.ID.eq(id)).get().first();
        return person;
    }
}
