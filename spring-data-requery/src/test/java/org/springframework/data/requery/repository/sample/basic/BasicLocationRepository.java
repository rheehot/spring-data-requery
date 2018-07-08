package org.springframework.data.requery.repository.sample.basic;

import org.springframework.data.requery.domain.basic.BasicLocation;
import org.springframework.data.requery.repository.RequeryRepository;

/**
 * BasicLocationRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface BasicLocationRepository extends RequeryRepository<BasicLocation, Integer> {
}
