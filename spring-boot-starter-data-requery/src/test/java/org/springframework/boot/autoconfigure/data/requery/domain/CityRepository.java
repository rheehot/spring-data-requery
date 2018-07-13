package org.springframework.boot.autoconfigure.data.requery.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * org.springframework.boot.autoconfigure.data.requery.domain.CityRepository
 *
 * @author debop
 */
public interface CityRepository extends Repository<City, Long> {


    Page<City> findByNameLikeAndCountryLikeAllIgnoringCase(String name, String country, Pageable pageable);

    City findByNameAndCountryAllIgnoringCase(String name, String country);
}
