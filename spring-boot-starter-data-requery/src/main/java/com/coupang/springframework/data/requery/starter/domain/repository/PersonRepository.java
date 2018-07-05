package com.coupang.springframework.data.requery.starter.domain.repository;

import org.springframework.data.requery.repository.RequeryRepository;
import org.springframework.data.requery.starter.domain.model.Person;

/**
 * @author Diego on 2018. 7. 1..
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
public interface PersonRepository extends RequeryRepository<Person, Long> {

}
