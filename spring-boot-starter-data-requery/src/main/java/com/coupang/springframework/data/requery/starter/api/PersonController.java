package com.coupang.springframework.data.requery.starter.api;

import com.coupang.springframework.data.requery.starter.dto.PersonDto;
import com.coupang.springframework.data.requery.starter.service.PersonService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Diego on 2018. 7. 1..
 */
@RestController
@RequestMapping("api/v1/person")
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(@NotNull PersonService personService) {
        this.service = personService;
    }

    // TODO: DTO, repository, service 사용하기
    @GetMapping("/{id}")
    public PersonDto get(@PathVariable("id") Long id) {
        if (id == null) {
            return null;
        }
        return service.getPersonById(id);
    }

    @PostMapping
    public PersonDto insert(@RequestBody PersonDto personDto) {
        // TODO: add insert
        return personDto;
    }

    @PutMapping
    public PersonDto upsert(@RequestBody PersonDto personDto) {
        // TODO: add upsert
        return personDto;
    }

    @PutMapping("/batch")
    public List<PersonDto> upsertAll(@RequestBody List<PersonDto> personDtos) {
        // TODO: add upsertAll
        return personDtos;
    }

    @DeleteMapping
    public PersonDto delete(@RequestBody PersonDto personDto) {
        // TODO: add delete
        return personDto;
    }

    @DeleteMapping("/batch")
    public List<PersonDto> deleteAll(@RequestBody List<PersonDto> personDtos) {
    // TODO: add delete
        return personDtos;
    }
}
