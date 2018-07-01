package com.coupang.springframework.data.requery.starter.api;

import com.coupang.springframework.data.requery.starter.dto.PersonDto;
import com.coupang.springframework.data.requery.starter.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Diego on 2018. 7. 1..
 */
@RestController
@RequestMapping("api/v1/person")
public class PersonController {

    @Autowired
    private PersonService service;

    // TODO DTO, repository, service 사용하기
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PersonDto getPerson(@PathVariable("id") Long id) {
        if (id == null) {
            return null;
        }
        PersonDto person = service.getPersonById(id);
        return person;
    }
}
