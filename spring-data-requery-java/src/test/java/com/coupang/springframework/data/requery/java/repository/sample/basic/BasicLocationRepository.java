package com.coupang.springframework.data.requery.java.repository.sample.basic;

import com.coupang.springframework.data.requery.java.domain.basic.BasicLocation;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * BasicLocationRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface BasicLocationRepository extends RequeryRepository<BasicLocation, Integer> {
}
