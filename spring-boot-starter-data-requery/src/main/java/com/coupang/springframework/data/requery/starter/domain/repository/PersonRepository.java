package com.coupang.springframework.data.requery.starter.domain.repository;

import com.coupang.springframework.data.requery.repository.RequeryRepository;
import com.coupang.springframework.data.requery.starter.domain.model.Person;

/**
 * @author Diego on 2018. 7. 1..
 */
@SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
public interface PersonRepository extends RequeryRepository<Person, Long> {

}
