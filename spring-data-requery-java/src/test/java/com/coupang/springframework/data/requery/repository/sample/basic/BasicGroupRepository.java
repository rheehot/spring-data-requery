package com.coupang.springframework.data.requery.repository.sample.basic;

import com.coupang.springframework.data.requery.domain.basic.BasicGroup;
import com.coupang.springframework.data.requery.repository.RequeryRepository;

/**
 * BasicGroupRepository
 *
 * @author debop@coupang.com
 * @since 18. 6. 12
 */
public interface BasicGroupRepository extends RequeryRepository<BasicGroup, Integer> {
}